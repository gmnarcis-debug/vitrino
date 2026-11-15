package red.line.instayar.ui.editor

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.components.auroraGlowBackground
import red.line.instayar.ui.screens.VipModalBottomSheet
import red.line.instayar.ui.theme.BrandCyan
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.DarkSurface
import red.line.instayar.ui.theme.DarkSurfaceCard
import red.line.instayar.ui.theme.DarkSurfaceCardElevated
import red.line.instayar.ui.theme.PrimaryGradientBrush
import red.line.instayar.ui.theme.VipGold
import red.line.instayar.ui.theme.VipGoldDark
import red.line.instayar.ui.theme.VipGoldLight
import red.line.instayar.util.ImageExportUtil
import red.line.instayar.util.PersianDateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.Bitmap
import red.line.instayar.billing.MyketBillingManager
import red.line.instayar.billing.BillingResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    templateId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    editorViewModel: EditorViewModel = viewModel(),
    billingManager: MyketBillingManager? = null,
    onPurchasePlan: ((String, (BillingResult) -> Unit) -> Unit)? = null,
    onRestorePurchases: (((Boolean, String) -> Unit) -> Unit)? = null
) {
    val state by editorViewModel.uiState.collectAsState()

    var showExitConfirmDialog by remember { mutableStateOf(false) }
    var showVipBottomSheet by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val permanentPath = try {
                val imagesDir = java.io.File(context.filesDir, "project_images").apply {
                    if (!exists()) mkdirs()
                }
                val destFile = java.io.File(imagesDir, "img_${System.currentTimeMillis()}.png")
                context.contentResolver.openInputStream(it)?.use { input ->
                    java.io.FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                destFile.absolutePath
            } catch (e: Exception) {
                android.util.Log.e("EditorScreen", "Error saving picked image", e)
                it.toString()
            }
            editorViewModel.replaceSelectedElementImage(permanentPath)
        }
    }

    val handleBackPress: () -> Unit = {
        if (state.canUndo) {
            showExitConfirmDialog = true
        } else {
            onNavigateBack()
        }
    }

    BackHandler {
        handleBackPress()
    }

    LaunchedEffect(templateId) {
        editorViewModel.loadTemplate(templateId)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.integrityError != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "خطای امنیتی اصالت برنامه ⚠️",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = state.integrityError ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("بازگشت به صفحه اصلی", color = Color.White)
                }
            }
        } else if (state.isLoading || state.currentTemplate == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandPurple)
            }
        } else {
            val template = state.currentTemplate!!
            val selectedElement = template.elements.find { it.id == state.selectedElementId }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // 1. Top Action Bar
                EditorTopBar(
                    canUndo = state.canUndo,
                    canRedo = state.canRedo,
                    isSaving = state.isSaving,
                    formatTitle = template.format.titleFa,
                    onBackClick = handleBackPress,
                    onUndoClick = { editorViewModel.undo() },
                    onRedoClick = { editorViewModel.redo() },
                    onExportClick = {
                        if (editorViewModel.hasUnreplacedSampleImages()) {
                            editorViewModel.setUnreplacedImageWarningDialog(true)
                        } else {
                            editorViewModel.openExportDialog()
                        }
                    }
                )

                // نوار یادآوری اضافه شدن واترمارک برای کاربران نسخه رایگان (کپسول شیشه‌ای شیک)
                if (!state.isVipUser) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassSurface(
                            shape = RoundedCornerShape(20.dp),
                            backgroundColor = Color(0x2A1E1605),
                            borderColor = Color(0x66F59E0B),
                            modifier = Modifier
                                .clickable { showVipBottomSheet = true }
                                .testTag("editor_watermark_pill")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Info,
                                    contentDescription = null,
                                    tint = VipGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "واترمارک • حذف با VIP 👑",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFFDE68A),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                    contentDescription = null,
                                    tint = VipGold,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }

                // 2. Interactive Canvas Viewport (Center)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .auroraGlowBackground(),
                    contentAlignment = Alignment.Center
                ) {
                    EditorInteractiveCanvas(
                        template = template,
                        selectedElementId = state.selectedElementId,
                        brandProfile = state.brandProfile,
                        isAdjustingInsideFrame = state.isAdjustingInsideFrame,
                        onSelectElement = { editorViewModel.selectElement(it) },
                        onDragElement = { dx, dy -> editorViewModel.updateElementPosition(dx, dy) },
                        onTransformElement = { scale, rot -> editorViewModel.updateElementTransform(scale, rot) },
                        onPanZoomInsideFrame = { panDx, panDy, zoomFactor ->
                            editorViewModel.panAndZoomInsideFrame(panDx, panDy, zoomFactor)
                        },
                        onGestureEnd = { editorViewModel.commitTransformGesture() },
                        onRequestPickImage = {
                            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    )

                    // In-frame adjust active indicator
                    if (state.isAdjustingInsideFrame) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xE6F59E0B))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "حالت کادربندی عکس: زوم دو انگشتی و جابه‌جایی ✋",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Auto-save feedback toast
                    if (state.saveMessage != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xE610B981))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = state.saveMessage ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // 3. Bottom Tool Panels
                EditorBottomControlPanel(
                    template = template,
                    selectedElement = selectedElement,
                    activeTab = state.activeToolTab,
                    isVipUser = state.isVipUser,
                    isAdjustingInsideFrame = state.isAdjustingInsideFrame,
                    onTabSelected = { editorViewModel.setActiveToolTab(it) },
                    onToggleAdjustInsideFrame = { editorViewModel.toggleAdjustingInsideFrame() },
                    onUpdateText = { editorViewModel.updateElementText(it) },
                    onUpdateFontSize = { editorViewModel.updateElementFontSize(it) },
                    onUpdateTextColor = { editorViewModel.updateElementTextColor(it) },
                    onUpdateTextAlign = { editorViewModel.updateElementTextAlign(it) },
                    onUpdateFontFamily = { editorViewModel.updateElementFontFamily(it) },
                    onUpdateOutline = { color, width -> editorViewModel.updateElementOutline(color, width) },
                    onUpdateOpacity = { editorViewModel.updateElementOpacity(it) },
                    onUpdateLetterSpacing = { editorViewModel.updateElementLetterSpacing(it) },
                    onUpdatePrices = { oldP, newP, unit -> editorViewModel.updateElementPrices(oldP, newP, unit) },
                    onUpdateBackgroundColor = { editorViewModel.updateElementBackgroundColor(it) },
                    onUpdateCornerRadius = { editorViewModel.updateElementCornerRadius(it) },
                    onUpdateBackground = { c1, c2 -> editorViewModel.updateBackgroundColors(c1, c2) },
                    onAddNewElement = { type, text -> editorViewModel.addNewElement(type, text) },
                    onBringForward = { editorViewModel.bringElementForward(it) },
                    onSendBackward = { editorViewModel.sendElementBackward(it) },
                    onDuplicate = { editorViewModel.duplicateElement(it) },
                    onDelete = { editorViewModel.deleteElement(it) },
                    onSelectElement = { editorViewModel.selectElement(it) },
                    onRequestPickImage = {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onUpdateContentScale = { editorViewModel.updateElementContentScale(it) },
                    onUpdateImagePanZoom = { px, py, z -> editorViewModel.updateElementImagePanAndZoom(px, py, z) },
                    onResetImagePanZoom = { editorViewModel.resetElementImagePanAndZoom() },
                    onOpenVipSheet = { showVipBottomSheet = true }
                )
            }
        }
    }

    // Unreplaced sample image warning dialog before export
    if (state.showUnreplacedImageWarningDialog) {
        val isDark = MaterialTheme.colorScheme.background == DarkBackground
        AlertDialog(
            onDismissRequest = { editorViewModel.setUnreplacedImageWarningDialog(false) },
            title = {
                Text(
                    text = "عکس نمونه در قالب 🖼️",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "عکس نمونه را با عکس محصول خودتان جایگزین کنید تا طرح اختصاصی فروشگاه شما تولید شود.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        editorViewModel.setUnreplacedImageWarningDialog(false)
                        editorViewModel.openExportDialog()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ادامه بدون تغییر", color = Color.White)
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = {
                            editorViewModel.setUnreplacedImageWarningDialog(false)
                        }
                    ) {
                        Text("انصراف", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(
                        onClick = {
                            editorViewModel.setUnreplacedImageWarningDialog(false)
                            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPink),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("انتخاب عکس محصول 📷", color = Color.White)
                    }
                }
            },
            containerColor = if (isDark) DarkSurfaceCardElevated else Color.White,
            shape = RoundedCornerShape(22.dp)
        )
    }

    // 4. Export Bottom Sheet with real preview, watermark, and VIP upgrade CTA
    if (state.showExportDialog && state.currentTemplate != null) {
        ExportBottomSheet(
            template = state.currentTemplate!!,
            brandProfile = state.brandProfile,
            isVipUser = state.isVipUser,
            isExporting = state.isExporting,
            exportFormat = state.exportFormat,
            exportQuality = state.exportQuality,
            exportError = state.exportError,
            onFormatSelected = { editorViewModel.setExportFormat(it) },
            onQualityChanged = { editorViewModel.setExportQuality(it) },
            onDismiss = { editorViewModel.closeExportDialog() },
            onSaveToGallery = {
                editorViewModel.exportToGallery(context) { success ->
                    if (success) {
                        editorViewModel.closeExportDialog()
                    }
                }
            },
            onShareInstagram = {
                editorViewModel.shareToInstagram(context)
                editorViewModel.closeExportDialog()
            },
            onOpenVip = {
                editorViewModel.closeExportDialog()
                showVipBottomSheet = true
            }
        )
    }

    // VIP Bottom Sheet
    if (showVipBottomSheet) {
        VipModalBottomSheet(
            billingManager = billingManager,
            onDismiss = { showVipBottomSheet = false },
            onPurchasePlan = { sku, onResult ->
                if (onPurchasePlan != null) {
                    onPurchasePlan(sku, onResult)
                } else {
                    onResult(BillingResult.Error("برای فعال‌سازی اشتراک، از منوی صفحه اصلی اقدام کنید."))
                }
            },
            onRestorePurchases = { onResult ->
                if (onRestorePurchases != null) {
                    onRestorePurchases(onResult)
                } else {
                    onResult(false, "خریدی یافت نشد.")
                }
            }
        )
    }

    // Unsaved Changes Confirmation Dialog
    if (showExitConfirmDialog) {
        val isDark = MaterialTheme.colorScheme.background == DarkBackground
        AlertDialog(
            onDismissRequest = { showExitConfirmDialog = false },
            title = {
                Text(
                    text = "خروج از ویرایشگر ⚠️",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "تغییراتی در این قالب اعمال کرده‌اید. پروژه به طور خودکار ذخیره شده است. آیا مایل به خروج هستید؟",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("خروج", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitConfirmDialog = false }
                ) {
                    Text("ادامه ویرایش", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = if (isDark) DarkSurfaceCardElevated else Color.White,
            shape = RoundedCornerShape(22.dp)
        )
    }
}

@Composable
private fun EditorTopBar(
    canUndo: Boolean,
    canRedo: Boolean,
    isSaving: Boolean = false,
    formatTitle: String,
    onBackClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onExportClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button with GlassSurface
        GlassSurface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .clickable { onBackClick() }
                .testTag("editor_back_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "بازگشت",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "خروج",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Center Format & Undo/Redo with GlassSurface
        GlassSurface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                IconButton(
                    onClick = onUndoClick,
                    enabled = canUndo,
                    modifier = Modifier.size(36.dp).testTag("editor_undo_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Undo,
                        contentDescription = "Undo",
                        tint = if (canUndo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSaving) Color(0x3310B981) else Color(0x338B5CF6))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isSaving) "ذخیره... 💾" else formatTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSaving) Color(0xFF34D399) else BrandCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onRedoClick,
                    enabled = canRedo,
                    modifier = Modifier.size(36.dp).testTag("editor_redo_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Redo,
                        contentDescription = "Redo",
                        tint = if (canRedo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }
        }

        // Export Button with gradient
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryGradientBrush)
                .clickable { onExportClick() }
                .padding(horizontal = 16.dp, vertical = 9.dp)
                .testTag("editor_export_button"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "خروجی 📥",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExportBottomSheet(
    template: red.line.instayar.data.model.Template,
    brandProfile: red.line.instayar.data.model.BrandProfile,
    isVipUser: Boolean,
    isExporting: Boolean,
    exportFormat: String,
    exportQuality: Int,
    exportError: String?,
    onFormatSelected: (String) -> Unit,
    onQualityChanged: (Int) -> Unit,
    onDismiss: () -> Unit,
    onSaveToGallery: () -> Unit,
    onShareInstagram: () -> Unit,
    onOpenVip: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    // رندر پیش‌نمایش واقعی خروجی (دقیقاً با همان واترمارک)
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isRenderingPreview by remember { mutableStateOf(true) }

    LaunchedEffect(template, isVipUser) {
        isRenderingPreview = true
        withContext(Dispatchers.Default) {
            try {
                previewBitmap = ImageExportUtil.renderTemplateToBitmap(
                    template = template,
                    brandProfile = brandProfile,
                    context = context
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isRenderingPreview = false
            }
        }
    }

    val resolution = when (template.format) {
        red.line.instayar.data.model.TemplateFormat.STORY -> "۱۰۸۰ × ۱۹۲۰ پیکسل (Full HD)"
        red.line.instayar.data.model.TemplateFormat.POST_PORTRAIT -> "۱۰۸۰ × ۱۳۵۰ پیکسل"
        red.line.instayar.data.model.TemplateFormat.POST_SQUARE -> "۱۰۸۰ × ۱۰۸۰ پیکسل"
        red.line.instayar.data.model.TemplateFormat.HIGHLIGHT -> "۱۰۸۰ × ۱۰۸۰ پیکسل"
    }

    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) DarkSurface else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "پیش‌نمایش و دریافت خروجی 🖼️",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ابعاد خروجی: $resolution",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // قاب پیش‌نمایش واقعی خروجی با واترمارک (شیشه‌ای با گوشه ۲۴dp)
            GlassSurface(
                shape = RoundedCornerShape(24.dp),
                backgroundColor = Color(0x33121124),
                borderColor = if (isVipUser) VipGold else Color(0x337C5CFF),
                borderWidth = 1.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (previewBitmap != null) {
                    Image(
                        bitmap = previewBitmap!!.asImageBitmap(),
                        contentDescription = "پیش‌نمایش خروجی واقعی",
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    CircularProgressIndicator(
                        color = BrandPurple,
                        modifier = Modifier.size(32.dp).align(Alignment.Center),
                        strokeWidth = 2.5.dp
                    )
                }

                // نشان برچسب وضعیت پیش‌نمایش
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isVipUser) Color(0xE62E1F00) else Color(0xDD0F0C1E))
                        .border(
                            0.5.dp,
                            if (isVipUser) VipGold else Color(0x66FFFFFF),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (isVipUser) "✨ پیش‌نمایش بدون واترمارک (VIP)" else "⚠️ پیش‌نمایش با واترمارک نسخه رایگان",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isVipUser) VipGold else Color(0xFFFDE68A)
                    )
                }
            }

            // دکمه بزرگ ترغیب به حذف واترمارک با ارتقا به VIP
            if (!isVipUser) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onOpenVip,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(VipGoldDark, VipGold, VipGoldLight)
                            )
                        )
                        .testTag("remove_watermark_vip_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color(0xFF2E1F00),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "حذف دائم واترمارک با نسخه طلایی VIP 👑",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E1F00),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // انتخاب فرمت خروجی (PNG vs JPEG)
            GlassSurface(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isDark) Color(0x331E1B3A) else Color(0x10000000),
                borderColor = if (isDark) Color(0x22FFFFFF) else Color(0x18000000),
                modifier = Modifier.padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isPng = exportFormat.equals("PNG", ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isPng) BrandPurple else Color.Transparent)
                            .clickable { onFormatSelected("PNG") }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "PNG (کیفیت اصلی)",
                            fontSize = 12.sp,
                            fontWeight = if (isPng) FontWeight.Bold else FontWeight.Normal,
                            color = if (isPng) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (!isPng) BrandPurple else Color.Transparent)
                            .clickable { onFormatSelected("JPEG") }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "JPEG (کم‌حجم)",
                            fontSize = 12.sp,
                            fontWeight = if (!isPng) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isPng) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // اسلایدر کیفیت JPEG
            if (exportFormat.equals("JPEG", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = PersianDateUtil.toPersianDigits("کیفیت فشرده‌سازی: ٪$exportQuality"),
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandCyan,
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (exportQuality >= 90) "کیفیت بسیار عالی" else "فشرده و سبک",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Slider(
                    value = exportQuality.toFloat(),
                    onValueChange = { onQualityChanged(it.toInt()) },
                    valueRange = 60f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandCyan,
                        activeTrackColor = BrandCyan
                    )
                )
            }

            // نمایش پیام خطای احتمالی
            if (exportError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x33EF4444))
                        .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = exportError,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFCA5A5),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isExporting) {
                CircularProgressIndicator(
                    color = BrandPurple,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "در حال پردازش و رندرینگ عکس...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontSize = 12.sp
                )
            } else {
                // دکمه ذخیره در گالری
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PrimaryGradientBrush)
                        .clickable { onSaveToGallery() }
                        .testTag("save_to_gallery_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ذخیره در گالری گوشی 💾",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // دکمه اشتراک‌گذاری در اینستاگرام
                GlassSurface(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = if (isDark) Color(0x22FFFFFF) else Color(0xF2FFFFFF),
                    borderColor = if (isDark) Color(0x338B5CF6) else Color(0x228B5CF6),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clickable { onShareInstagram() }
                        .testTag("share_instagram_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Rounded.Share, contentDescription = null, tint = BrandPink)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "اشتراک‌گذاری مستقیم در اینستاگرام 📲",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
