package red.line.instayar.ui.editor

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.data.model.ElementType
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateBackground
import red.line.instayar.data.model.TemplateElement
import red.line.instayar.data.repository.ProjectRepository
import red.line.instayar.data.repository.TemplateRepository
import red.line.instayar.data.repository.UserPreferences
import red.line.instayar.util.ImageExportUtil
import red.line.instayar.security.AppIntegrityChecker
import red.line.instayar.security.IntegrityResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditorUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isExporting: Boolean = false,
    val currentTemplate: Template? = null,
    val selectedElementId: String? = null,
    val isAdjustingInsideFrame: Boolean = false,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val activeToolTab: EditorToolTab = EditorToolTab.TEXT,
    val brandProfile: BrandProfile = BrandProfile(),
    val isVipUser: Boolean = false,
    val showExportDialog: Boolean = false,
    val showUnreplacedImageWarningDialog: Boolean = false,
    val exportFormat: String = "PNG", // "PNG" or "JPEG"
    val exportQuality: Int = 90, // 60..100
    val exportError: String? = null,
    val saveMessage: String? = null,
    val integrityError: String? = null
)

enum class EditorToolTab(val titleFa: String) {
    TEXT("متن"),
    PRICE("قیمت"),
    IMAGE("عکس"),
    STYLE("استایل و رنگ"),
    BACKGROUND("پس‌زمینه"),
    ELEMENTS("افزودن المان"),
    LAYERS("لایه‌ها")
}

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TemplateRepository(application)
    private val projectRepository = ProjectRepository(application)
    private val preferences = UserPreferences(application)

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    // History stacks for Undo / Redo with memory safety bound (max 30 states)
    private val maxHistorySize = 30
    private val history = mutableListOf<Template>()
    private var historyIndex = -1

    // Debounced Auto-save Job
    private var autoSaveJob: kotlinx.coroutines.Job? = null

    init {
        viewModelScope.launch {
            preferences.brandProfile.collect { profile ->
                _uiState.update { it.copy(brandProfile = profile) }
            }
        }
        viewModelScope.launch {
            preferences.isVipUser.collect { isVip ->
                _uiState.update { it.copy(isVipUser = isVip) }
            }
        }
    }

    fun loadTemplate(templateId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val integrity = AppIntegrityChecker.verifyAppIntegrity(getApplication())
            if (integrity is IntegrityResult.Compromised) {
                _uiState.update { it.copy(isLoading = false, integrityError = integrity.reasonFa) }
                return@launch
            }

            var template = projectRepository.getProjectTemplate(templateId)
            if (template == null) {
                // Efficient targeted load from memory cache / direct asset
                template = repository.getTemplateById(templateId)
            }
            if (template == null) {
                val all = repository.loadTemplates()
                template = all.find { it.id == templateId } ?: all.firstOrNull()
            }

            if (template != null) {
                history.clear()
                history.add(template)
                historyIndex = 0
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentTemplate = template,
                        selectedElementId = template.elements.firstOrNull()?.id,
                        canUndo = false,
                        canRedo = false
                    )
                }
                projectRepository.saveProject(template)
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun scheduleAutoSave(templateToSave: Template) {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            kotlinx.coroutines.delay(600) // 600ms debounce
            try {
                projectRepository.saveProject(templateToSave)
            } catch (e: Exception) {
                android.util.Log.e("EditorViewModel", "Failed auto-saving project", e)
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun pushHistory(newTemplate: Template) {
        // Drop any redos after current index
        while (history.size > historyIndex + 1) {
            history.removeAt(history.size - 1)
        }
        history.add(newTemplate)

        // Bound history memory footprint
        if (history.size > maxHistorySize) {
            history.removeAt(0)
        }
        historyIndex = history.size - 1

        _uiState.update {
            it.copy(
                currentTemplate = newTemplate,
                canUndo = historyIndex > 0,
                canRedo = false
            )
        }
        scheduleAutoSave(newTemplate)
    }

    fun undo() {
        if (historyIndex > 0) {
            historyIndex--
            val previousTemplate = history[historyIndex]
            _uiState.update {
                it.copy(
                    currentTemplate = previousTemplate,
                    canUndo = historyIndex > 0,
                    canRedo = historyIndex < history.size - 1
                )
            }
            scheduleAutoSave(previousTemplate)
        }
    }

    fun redo() {
        if (historyIndex < history.size - 1) {
            historyIndex++
            val nextTemplate = history[historyIndex]
            _uiState.update {
                it.copy(
                    currentTemplate = nextTemplate,
                    canUndo = historyIndex > 0,
                    canRedo = historyIndex < history.size - 1
                )
            }
            scheduleAutoSave(nextTemplate)
        }
    }

    fun selectElement(elementId: String?) {
        val tpl = _uiState.value.currentTemplate
        val el = tpl?.elements?.find { it.id == elementId }
        val newTab = when {
            el == null -> _uiState.value.activeToolTab
            el.type == ElementType.IMAGE || el.type == ElementType.IMAGE_PLACEHOLDER -> {
                EditorToolTab.IMAGE
            }
            el.type == ElementType.PRICE_BLOCK -> {
                EditorToolTab.PRICE
            }
            el.type == ElementType.TEXT || el.type == ElementType.BRAND_NAME || el.type == ElementType.BRAND_CONTACT -> {
                EditorToolTab.TEXT
            }
            else -> _uiState.value.activeToolTab
        }
        _uiState.update { it.copy(selectedElementId = elementId, activeToolTab = newTab, isAdjustingInsideFrame = false) }
    }

    fun setActiveToolTab(tab: EditorToolTab) {
        _uiState.update { it.copy(activeToolTab = tab) }
    }

    fun setAdjustingInsideFrame(isAdjusting: Boolean) {
        _uiState.update { it.copy(isAdjustingInsideFrame = isAdjusting) }
    }

    fun toggleAdjustingInsideFrame() {
        _uiState.update { it.copy(isAdjustingInsideFrame = !it.isAdjustingInsideFrame) }
    }

    fun updateElementPosition(dxRel: Float, dyRel: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updatedElements = tpl.elements.map { el ->
            if (el.id == id) {
                el.copy(
                    x = (el.x + dxRel).coerceIn(-0.3f, 1.0f),
                    y = (el.y + dyRel).coerceIn(-0.3f, 1.0f)
                )
            } else el
        }
        val newTpl = tpl.copy(elements = updatedElements)
        _uiState.update { it.copy(currentTemplate = newTpl) }
    }

    fun commitTransformGesture() {
        val tpl = _uiState.value.currentTemplate ?: return
        pushHistory(tpl)
    }

    fun updateElementTransform(scaleFactor: Float, deltaRotation: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updatedElements = tpl.elements.map { el ->
            if (el.id == id) {
                val newW = (el.width * scaleFactor).coerceIn(0.08f, 1.2f)
                val newH = (el.height * scaleFactor).coerceIn(0.02f, 1.2f)
                val newRot = (el.rotation + deltaRotation) % 360f
                el.copy(
                    width = newW,
                    height = newH,
                    rotation = newRot
                )
            } else el
        }
        val newTpl = tpl.copy(elements = updatedElements)
        _uiState.update { it.copy(currentTemplate = newTpl) }
    }

    // 1. Gesture pan and zoom inside frame
    fun panAndZoomInsideFrame(panDx: Float, panDy: Float, zoomFactor: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) {
                val currentScale = el.effectiveScale
                val newScale = (currentScale * zoomFactor).coerceIn(0.5f, 5.0f)
                val newOffsetX = (el.effectiveOffsetX + panDx).coerceIn(-1.5f, 1.5f)
                val newOffsetY = (el.effectiveOffsetY + panDy).coerceIn(-1.5f, 1.5f)
                el.copy(
                    imageScale = newScale,
                    imageOffsetX = newOffsetX,
                    imageOffsetY = newOffsetY,
                    imageZoom = newScale,
                    imagePanX = newOffsetX,
                    imagePanY = newOffsetY
                )
            } else el
        }
        _uiState.update { it.copy(currentTemplate = tpl.copy(elements = updated)) }
    }

    fun updateElementText(newText: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(text = newText) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementFontSize(newSize: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(fontSize = newSize.coerceIn(10f, 60f)) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementTextColor(colorHex: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(textColor = colorHex) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementTextAlign(align: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(textAlign = align) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    // 3. New Text Typography & Styling methods
    fun updateElementFontFamily(fontFamily: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(fontFamily = fontFamily) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementOutline(outlineColor: String?, outlineWidth: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(outlineColor = outlineColor, outlineWidth = outlineWidth) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementOpacity(opacity: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(opacity = opacity.coerceIn(0.1f, 1f)) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementLetterSpacing(spacing: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(letterSpacing = spacing.coerceIn(-0.1f, 0.5f)) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    // 5. Price Block update methods
    fun updateElementPrices(oldPrice: Long?, newPrice: Long?, currencyUnit: String = "تومان") {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) {
                el.copy(
                    oldPrice = oldPrice,
                    newPrice = newPrice,
                    currencyUnit = currencyUnit
                )
            } else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementBackgroundColor(colorHex: String?) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(backgroundColor = colorHex) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementBorder(borderColorHex: String?, borderWidth: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(borderColor = borderColorHex, borderWidth = borderWidth) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementCornerRadius(radius: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(cornerRadius = radius) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun replaceSelectedElementImage(uriString: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) {
                el.copy(
                    type = if (el.type == ElementType.IMAGE_PLACEHOLDER) ElementType.IMAGE else el.type,
                    imageUri = uriString,
                    imagePanX = 0f,
                    imagePanY = 0f,
                    imageZoom = 1f,
                    imageScale = 1f,
                    imageOffsetX = 0f,
                    imageOffsetY = 0f
                )
            } else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementContentScale(scale: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) el.copy(contentScale = scale) else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun updateElementImagePanAndZoom(panX: Float, panY: Float, zoom: Float) {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) {
                el.copy(
                    imagePanX = panX.coerceIn(-1.5f, 1.5f),
                    imagePanY = panY.coerceIn(-1.5f, 1.5f),
                    imageZoom = zoom.coerceIn(0.5f, 5.0f),
                    imageOffsetX = panX.coerceIn(-1.5f, 1.5f),
                    imageOffsetY = panY.coerceIn(-1.5f, 1.5f),
                    imageScale = zoom.coerceIn(0.5f, 5.0f)
                )
            } else el
        }
        _uiState.update { it.copy(currentTemplate = tpl.copy(elements = updated)) }
    }

    fun resetElementImagePanAndZoom() {
        val tpl = _uiState.value.currentTemplate ?: return
        val id = _uiState.value.selectedElementId ?: return

        val updated = tpl.elements.map { el ->
            if (el.id == id) {
                el.copy(
                    imagePanX = 0f,
                    imagePanY = 0f,
                    imageZoom = 1f,
                    imageScale = 1f,
                    imageOffsetX = 0f,
                    imageOffsetY = 0f
                )
            } else el
        }
        pushHistory(tpl.copy(elements = updated))
    }

    fun hasUnreplacedSampleImages(): Boolean {
        val tpl = _uiState.value.currentTemplate ?: return false
        return tpl.elements.any { el ->
            (el.type == ElementType.IMAGE || el.type == ElementType.IMAGE_PLACEHOLDER) &&
                    !el.imageAsset.isNullOrBlank() &&
                    el.imageUri.isNullOrBlank()
        }
    }

    fun setUnreplacedImageWarningDialog(show: Boolean) {
        _uiState.update { it.copy(showUnreplacedImageWarningDialog = show) }
    }

    fun updateBackgroundColors(primaryColor: String, secondaryColor: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val newBg = tpl.background.copy(
            gradientColors = listOf(primaryColor, secondaryColor)
        )
        pushHistory(tpl.copy(background = newBg))
    }

    fun bringElementForward(elementId: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val list = tpl.elements.toMutableList()
        val index = list.indexOfFirst { it.id == elementId }
        if (index in 0 until list.size - 1) {
            val el = list.removeAt(index)
            list.add(index + 1, el)
            pushHistory(tpl.copy(elements = list))
        }
    }

    fun sendElementBackward(elementId: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val list = tpl.elements.toMutableList()
        val index = list.indexOfFirst { it.id == elementId }
        if (index > 0) {
            val el = list.removeAt(index)
            list.add(index - 1, el)
            pushHistory(tpl.copy(elements = list))
        }
    }

    fun duplicateElement(elementId: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val el = tpl.elements.find { it.id == elementId } ?: return
        val copy = el.copy(
            id = "el_${System.currentTimeMillis()}",
            x = (el.x + 0.05f).coerceAtMost(0.8f),
            y = (el.y + 0.05f).coerceAtMost(0.8f)
        )
        val list = tpl.elements.toMutableList().apply { add(copy) }
        pushHistory(tpl.copy(elements = list))
        selectElement(copy.id)
    }

    fun deleteElement(elementId: String) {
        val tpl = _uiState.value.currentTemplate ?: return
        val list = tpl.elements.filter { it.id != elementId }
        pushHistory(tpl.copy(elements = list))
        _uiState.update {
            it.copy(selectedElementId = list.lastOrNull()?.id)
        }
    }

    fun addNewElement(type: ElementType, defaultText: String? = null) {
        val tpl = _uiState.value.currentTemplate ?: return
        val newId = "el_${System.currentTimeMillis()}"
        val newEl = when (type) {
            ElementType.PRICE_BLOCK -> TemplateElement(
                id = newId,
                type = ElementType.PRICE_BLOCK,
                x = 0.15f,
                y = 0.72f,
                width = 0.7f,
                height = 0.14f,
                oldPrice = 980000L,
                newPrice = 690000L,
                currencyUnit = "تومان",
                fontSize = 18f,
                textColor = "#FFFFFF",
                backgroundColor = "#241B3B",
                borderColor = "#A855F7",
                borderWidth = 1.5f,
                cornerRadius = 18f
            )
            ElementType.TEXT -> TemplateElement(
                id = newId,
                type = ElementType.TEXT,
                x = 0.2f,
                y = 0.45f,
                width = 0.6f,
                height = 0.08f,
                text = defaultText ?: "متن جدید شما",
                fontSize = 20f,
                textColor = "#FFFFFF",
                fontWeight = "BOLD",
                textAlign = "CENTER",
                fontFamily = "vazirmatn"
            )
            ElementType.BADGE -> TemplateElement(
                id = newId,
                type = ElementType.BADGE,
                x = 0.3f,
                y = 0.45f,
                width = 0.4f,
                height = 0.05f,
                text = defaultText ?: "🔥 آفر ویژه",
                fontSize = 12f,
                textColor = "#FFFFFF",
                backgroundColor = "#E11D48"
            )
            ElementType.SHAPE -> TemplateElement(
                id = newId,
                type = ElementType.SHAPE,
                x = 0.25f,
                y = 0.45f,
                width = 0.5f,
                height = 0.09f,
                shapeType = "ROUNDED_RECT",
                cornerRadius = 16f,
                backgroundColor = "#7C3AED",
                borderColor = "#C084FC",
                borderWidth = 1.5f
            )
            ElementType.IMAGE -> TemplateElement(
                id = newId,
                type = ElementType.IMAGE,
                x = 0.2f,
                y = 0.25f,
                width = 0.6f,
                height = 0.45f,
                cornerRadius = 16f,
                imageAsset = "fashion_01.webp",
                contentScale = "CROP"
            )
            ElementType.IMAGE_PLACEHOLDER -> TemplateElement(
                id = newId,
                type = ElementType.IMAGE_PLACEHOLDER,
                x = 0.15f,
                y = 0.3f,
                width = 0.7f,
                height = 0.4f,
                cornerRadius = 20f,
                backgroundColor = "#334155",
                borderColor = "#38BDF8",
                borderWidth = 2f,
                placeholderLabel = "تصویر محصول",
                imageAsset = "fashion_01.webp",
                contentScale = "CROP"
            )
            ElementType.BRAND_LOGO -> TemplateElement(
                id = newId,
                type = ElementType.BRAND_LOGO,
                x = 0.4f,
                y = 0.06f,
                width = 0.2f,
                height = 0.1f,
                cornerRadius = 50f
            )
            else -> TemplateElement(
                id = newId,
                type = ElementType.TEXT,
                x = 0.2f,
                y = 0.45f,
                width = 0.6f,
                height = 0.08f,
                text = "المان جدید",
                fontSize = 16f,
                textColor = "#FFFFFF"
            )
        }
        val list = tpl.elements.toMutableList().apply { add(newEl) }
        pushHistory(tpl.copy(elements = list))
        selectElement(newId)
    }

    // 4. Export format and quality options
    fun setExportFormat(format: String) {
        _uiState.update { it.copy(exportFormat = format) }
    }

    fun setExportQuality(quality: Int) {
        _uiState.update { it.copy(exportQuality = quality.coerceIn(60, 100)) }
    }

    fun exportToGallery(context: Context, onComplete: (Boolean) -> Unit) {
        val integrity = AppIntegrityChecker.verifyAppIntegrity(context)
        if (integrity is IntegrityResult.Compromised) {
            showSaveFeedback(integrity.reasonFa)
            onComplete(false)
            return
        }

        val tpl = _uiState.value.currentTemplate ?: return
        val formatStr = _uiState.value.exportFormat
        val quality = _uiState.value.exportQuality
        val isJpeg = formatStr.equals("JPEG", ignoreCase = true)
        val compressFormat = if (isJpeg) Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG

        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, exportError = null) }
            val bitmap = ImageExportUtil.renderTemplateToBitmap(
                template = tpl,
                brandProfile = _uiState.value.brandProfile,
                context = context
            )
            val uri = ImageExportUtil.saveBitmapToGallery(
                context = context,
                bitmap = bitmap,
                title = tpl.title,
                compressFormat = compressFormat,
                quality = quality
            )
            _uiState.update { it.copy(isExporting = false) }

            if (uri != null) {
                projectRepository.saveProject(tpl)
                showSaveFeedback("طرح با موفقیت و فرمت $formatStr در گالری ذخیره شد! 🎉")
                onComplete(true)
            } else {
                val errorMsg = "خطا در ذخیره‌سازی تصویر در گالری! لطفا دسترسی ذخیره‌سازی را بررسی کنید."
                _uiState.update { it.copy(exportError = errorMsg) }
                showSaveFeedback(errorMsg)
                onComplete(false)
            }
        }
    }

    fun shareToInstagram(context: Context) {
        val integrity = AppIntegrityChecker.verifyAppIntegrity(context)
        if (integrity is IntegrityResult.Compromised) {
            showSaveFeedback(integrity.reasonFa)
            return
        }

        val tpl = _uiState.value.currentTemplate ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true) }
            val bitmap = ImageExportUtil.renderTemplateToBitmap(
                template = tpl,
                brandProfile = _uiState.value.brandProfile,
                context = context
            )
            val intent = ImageExportUtil.shareBitmap(context, bitmap, preferInstagram = true)
            _uiState.update { it.copy(isExporting = false) }

            if (intent != null) {
                context.startActivity(intent)
            } else {
                showSaveFeedback("خطا در آماده‌سازی اشتراک‌گذاری!")
            }
        }
    }

    fun openExportDialog() {
        _uiState.update { it.copy(showExportDialog = true, exportError = null) }
    }

    fun closeExportDialog() {
        _uiState.update { it.copy(showExportDialog = false, exportError = null) }
    }

    fun showSaveFeedback(message: String) {
        _uiState.update { it.copy(saveMessage = message) }
    }

    fun dismissSaveFeedback() {
        _uiState.update { it.copy(saveMessage = null) }
    }
}
