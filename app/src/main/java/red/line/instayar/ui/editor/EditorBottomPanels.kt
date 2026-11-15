package red.line.instayar.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FormatAlignCenter
import androidx.compose.material.icons.rounded.FormatAlignLeft
import androidx.compose.material.icons.rounded.FormatAlignRight
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PanTool
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.instayar.data.model.ElementType
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateElement
import red.line.instayar.ui.components.GlassSurface
import red.line.instayar.ui.theme.BrandCyan
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.DarkSurface
import red.line.instayar.ui.theme.DarkSurfaceCard
import red.line.instayar.ui.theme.DarkSurfaceCardElevated
import red.line.instayar.ui.theme.PrimaryGradientBrush
import red.line.instayar.util.PersianDateUtil

val LUXURY_PALETTE = listOf(
    "#FFFFFF",
    "#000000",
    "#F59E0B", // Gold
    "#EC4899", // Pink
    "#8B5CF6", // Purple
    "#06B6D4", // Cyan
    "#10B981", // Emerald
    "#EF4444", // Red
    "#3B82F6", // Blue
    "#64748B"  // Slate
)

val PRESET_BACKGROUNDS = listOf(
    Pair("ارغوانی شب", listOf("#2C1252", "#0F0B1E")),
    Pair("سرخابی حراج", listOf("#4C0519", "#1E020D")),
    Pair("اقیانوس لوکس", listOf("#1E40AF", "#0F172A")),
    Pair("زمرد تیره", listOf("#064E3B", "#022C22")),
    Pair("طلای شاهانه", listOf("#78350F", "#291500")),
    Pair("کربن بلک", listOf("#27272A", "#09090B"))
)

val PERSIAN_FONTS = listOf(
    Pair("vazirmatn", "وزیرمتن"),
    Pair("lalezar", "لاله‌زار"),
    Pair("parastoo", "پرستو"),
    Pair("gandom", "گندم"),
    Pair("sahel", "ساحل"),
    Pair("samim", "صمیم"),
    Pair("shabnam", "شبنم"),
    Pair("tanha", "تنها")
)

@Composable
fun EditorBottomControlPanel(
    template: Template,
    selectedElement: TemplateElement?,
    activeTab: EditorToolTab,
    isVipUser: Boolean,
    isAdjustingInsideFrame: Boolean = false,
    onTabSelected: (EditorToolTab) -> Unit,
    onToggleAdjustInsideFrame: () -> Unit = {},
    onUpdateText: (String) -> Unit,
    onUpdateFontSize: (Float) -> Unit,
    onUpdateTextColor: (String) -> Unit,
    onUpdateTextAlign: (String) -> Unit,
    onUpdateFontFamily: (String) -> Unit = {},
    onUpdateOutline: (colorHex: String?, width: Float) -> Unit = { _, _ -> },
    onUpdateOpacity: (Float) -> Unit = {},
    onUpdateLetterSpacing: (Float) -> Unit = {},
    onUpdatePrices: (oldPrice: Long?, newPrice: Long?, unit: String) -> Unit = { _, _, _ -> },
    onUpdateBackgroundColor: (String?) -> Unit,
    onUpdateCornerRadius: (Float) -> Unit,
    onUpdateBackground: (primaryColor: String, secondaryColor: String) -> Unit,
    onAddNewElement: (ElementType, String?) -> Unit,
    onBringForward: (String) -> Unit,
    onSendBackward: (String) -> Unit,
    onDuplicate: (String) -> Unit,
    onDelete: (String) -> Unit,
    onSelectElement: (String?) -> Unit,
    onRequestPickImage: () -> Unit = {},
    onUpdateContentScale: (String) -> Unit = {},
    onUpdateImagePanZoom: (panX: Float, panY: Float, zoom: Float) -> Unit = { _, _, _ -> },
    onResetImagePanZoom: () -> Unit = {},
    onOpenVipSheet: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(if (isDark) DarkSurface else MaterialTheme.colorScheme.surface)
            .border(1.dp, if (isDark) Color(0x338B5CF6) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .navigationBarsPadding()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        // دستگیره کشویی برای جمع کردن / باز کردن پانل جهت دیدن بهتر بوم
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0x55FFFFFF) else Color(0x33000000))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 1. Tool Tabs Row (کپسول‌های شناور شیشه‌ای با آیکون و گرادیانت تب فعال)
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(EditorToolTab.values().size) { index ->
                val tab = EditorToolTab.values()[index]
                val isSelected = activeTab == tab
                val tabIcon = when (tab) {
                    EditorToolTab.TEXT -> Icons.Rounded.TextFields
                    EditorToolTab.PRICE -> Icons.Rounded.Sell
                    EditorToolTab.IMAGE -> Icons.Rounded.Image
                    EditorToolTab.STYLE -> Icons.Rounded.Palette
                    EditorToolTab.BACKGROUND -> Icons.Rounded.Wallpaper
                    EditorToolTab.ELEMENTS -> Icons.Rounded.AddCircleOutline
                    EditorToolTab.LAYERS -> Icons.Rounded.Layers
                }

                val tabShape = RoundedCornerShape(16.dp)
                val tabModifier = if (isSelected) {
                    Modifier.background(Brush.horizontalGradient(listOf(BrandPurple, BrandPink)), tabShape)
                } else {
                    Modifier
                        .background(if (isDark) Color(0x18FFFFFF) else Color(0xF2F1F5F9), tabShape)
                        .border(1.dp, if (isDark) Color(0x22FFFFFF) else Color(0x18000000), tabShape)
                }

                Box(
                    modifier = Modifier
                        .clip(tabShape)
                        .then(tabModifier)
                        .clickable {
                            if (!isExpanded) isExpanded = true
                            onTabSelected(tab)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("editor_tab_${tab.name}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = tabIcon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = tab.titleFa,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 2. Active Tab Content (با انیمیشن باز و بسته شدن)
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                when (activeTab) {
                    EditorToolTab.TEXT -> {
                        TextToolPanel(
                            element = selectedElement,
                            isVipUser = isVipUser,
                            onUpdateText = onUpdateText,
                            onUpdateFontSize = onUpdateFontSize,
                            onUpdateTextColor = onUpdateTextColor,
                            onUpdateTextAlign = onUpdateTextAlign,
                            onUpdateFontFamily = onUpdateFontFamily,
                            onUpdateOutline = onUpdateOutline,
                            onUpdateOpacity = onUpdateOpacity,
                            onUpdateLetterSpacing = onUpdateLetterSpacing,
                            onOpenVipSheet = onOpenVipSheet
                        )
                    }
                    EditorToolTab.PRICE -> {
                        PriceToolPanel(
                            element = selectedElement,
                            onUpdatePrices = onUpdatePrices
                        )
                    }
                    EditorToolTab.IMAGE -> {
                        ImageToolPanel(
                            element = selectedElement,
                            isAdjustingInsideFrame = isAdjustingInsideFrame,
                            onToggleAdjustInsideFrame = onToggleAdjustInsideFrame,
                            onRequestPickImage = onRequestPickImage,
                            onUpdateContentScale = onUpdateContentScale,
                            onUpdateCornerRadius = onUpdateCornerRadius,
                            onUpdateImagePanZoom = onUpdateImagePanZoom,
                            onResetImagePanZoom = onResetImagePanZoom,
                            onDeleteImage = { selectedElement?.let { onDelete(it.id) } }
                        )
                    }
                    EditorToolTab.STYLE -> {
                        StyleToolPanel(
                            element = selectedElement,
                            onUpdateTextColor = onUpdateTextColor,
                            onUpdateBackgroundColor = onUpdateBackgroundColor,
                            onUpdateCornerRadius = onUpdateCornerRadius,
                            onUpdateOpacity = onUpdateOpacity
                        )
                    }
                    EditorToolTab.BACKGROUND -> {
                        BackgroundToolPanel(
                            onSelectPreset = onUpdateBackground
                        )
                    }
                    EditorToolTab.ELEMENTS -> {
                        AddElementsToolPanel(
                            onAddElement = onAddNewElement
                        )
                    }
                    EditorToolTab.LAYERS -> {
                        LayersToolPanel(
                            template = template,
                            selectedElementId = selectedElement?.id,
                            onBringForward = onBringForward,
                            onSendBackward = onSendBackward,
                            onDuplicate = onDuplicate,
                            onDelete = onDelete,
                            onSelectElement = onSelectElement
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TextToolPanel(
    element: TemplateElement?,
    isVipUser: Boolean,
    onUpdateText: (String) -> Unit,
    onUpdateFontSize: (Float) -> Unit,
    onUpdateTextColor: (String) -> Unit,
    onUpdateTextAlign: (String) -> Unit,
    onUpdateFontFamily: (String) -> Unit,
    onUpdateOutline: (colorHex: String?, width: Float) -> Unit,
    onUpdateOpacity: (Float) -> Unit,
    onUpdateLetterSpacing: (Float) -> Unit,
    onOpenVipSheet: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    if (element == null || (element.type != ElementType.TEXT && element.type != ElementType.BRAND_NAME && element.type != ElementType.BRAND_CONTACT)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "یک المان متنی را از روی بوم انتخاب کنید تا ویرایش شود.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    var textInput by remember(element.id, element.text) { mutableStateOf(element.text ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Text Input Field
        OutlinedTextField(
            value = textInput,
            onValueChange = {
                textInput = it
                onUpdateText(it)
            },
            placeholder = { Text("متن خود را اینجا بنویسید...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (isDark) DarkSurfaceCard else Color.White,
                unfocusedContainerColor = if (isDark) DarkSurfaceCard else Color.White,
                focusedBorderColor = BrandPurple,
                unfocusedBorderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1A000000),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // 3. Persian Font Selector (Real Persian fonts only)
        Text(
            text = "انتخاب فونت فارسی:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PERSIAN_FONTS.forEach { (fontKey, fontLabel) ->
                val isSelected = element.fontFamily?.equals(fontKey, ignoreCase = true) == true ||
                        (element.fontFamily == null && fontKey == "vazirmatn")
                val isVipFont = fontKey == "lalezar"

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) BrandCyan else (if (isDark) DarkSurfaceCard else Color(0xFFF1F5F9)))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) BrandCyan else (if (isDark) Color(0x22FFFFFF) else Color(0x15000000)),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (isVipFont && !isVipUser) {
                                onOpenVipSheet()
                            } else {
                                onUpdateFontFamily(fontKey)
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isVipFont) {
                            Text("👑 ", fontSize = 12.sp)
                        }
                        Text(
                            text = fontLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF0F172A) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Alignment & Size Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) DarkSurfaceCard else Color(0xFFF1F5F9))
                    .padding(2.dp)
            ) {
                IconButton(
                    onClick = { onUpdateTextAlign("RIGHT") },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FormatAlignRight,
                        contentDescription = "راست‌چین",
                        tint = if (element.textAlign == "RIGHT") BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = { onUpdateTextAlign("CENTER") },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FormatAlignCenter,
                        contentDescription = "وسط‌چین",
                        tint = if (element.textAlign == "CENTER") BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = { onUpdateTextAlign("LEFT") },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FormatAlignLeft,
                        contentDescription = "چپ‌چین",
                        tint = if (element.textAlign == "LEFT") BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Font size slider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(170.dp)
            ) {
                Text(
                    text = "${element.fontSize.toInt()}sp",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Slider(
                    value = element.fontSize,
                    onValueChange = onUpdateFontSize,
                    valueRange = 10f..46f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandPurple,
                        activeTrackColor = BrandPurple
                    )
                )
            }
        }

        // Text Outline & Opacity Sliders
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "کادر دور متن (استروک): ${element.outlineWidth.toInt()}px",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Slider(
                    value = element.outlineWidth,
                    onValueChange = { newW ->
                        onUpdateOutline(if (newW > 0f) (element.outlineColor ?: "#000000") else null, newW)
                    },
                    valueRange = 0f..8f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandPink,
                        activeTrackColor = BrandPink
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "شفافیت متن: ${(element.opacity * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Slider(
                    value = element.opacity,
                    onValueChange = onUpdateOpacity,
                    valueRange = 0.2f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandCyan,
                        activeTrackColor = BrandCyan
                    )
                )
            }
        }

        // Letter spacing slider
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "فاصله حروف: ${String.format(java.util.Locale.US, "%.2f", element.letterSpacing)}",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = element.letterSpacing,
                onValueChange = onUpdateLetterSpacing,
                valueRange = -0.05f..0.3f,
                colors = SliderDefaults.colors(
                    thumbColor = BrandPurple,
                    activeTrackColor = BrandPurple
                )
            )
        }

        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}

@Composable
private fun PriceToolPanel(
    element: TemplateElement?,
    onUpdatePrices: (oldPrice: Long?, newPrice: Long?, unit: String) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    if (element == null || element.type != ElementType.PRICE_BLOCK) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "یک بلوک قیمت (PRICE_BLOCK) را انتخاب کنید تا قیمت و تخفیف آن ویرایش شود.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    var oldPriceStr by remember(element.id, element.oldPrice) {
        mutableStateOf(element.oldPrice?.toString() ?: "")
    }
    var newPriceStr by remember(element.id, element.newPrice) {
        mutableStateOf(element.newPrice?.toString() ?: "")
    }
    var unitStr by remember(element.id, element.currencyUnit) {
        mutableStateOf(element.currencyUnit)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Live computed discount percentage pill
        val disc = element.discountPercentage
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "تنظیم قیمت و درصد تخفیف خودکار:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (disc > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEF4444))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = PersianDateUtil.toPersianDigits("تخفیف محاسبه‌شده: ٪$disc"),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Old Price Input
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "قیمت قبل (خط‌خورده):",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = oldPriceStr,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isDigit() }
                        oldPriceStr = filtered
                        val oldVal = filtered.toLongOrNull()
                        val newVal = newPriceStr.toLongOrNull()
                        onUpdatePrices(oldVal, newVal, unitStr)
                    },
                    placeholder = { Text("مثال: ۹۸۰۰۰۰", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) DarkSurfaceCard else Color.White,
                        unfocusedContainerColor = if (isDark) DarkSurfaceCard else Color.White,
                        focusedBorderColor = BrandPurple,
                        unfocusedBorderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1A000000),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )
            }

            // New Price Input
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "قیمت حراج / جدید:",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = newPriceStr,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isDigit() }
                        newPriceStr = filtered
                        val oldVal = oldPriceStr.toLongOrNull()
                        val newVal = filtered.toLongOrNull()
                        onUpdatePrices(oldVal, newVal, unitStr)
                    },
                    placeholder = { Text("مثال: ۶۹۰۰۰۰", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) DarkSurfaceCard else Color.White,
                        unfocusedContainerColor = if (isDark) DarkSurfaceCard else Color.White,
                        focusedBorderColor = BrandPurple,
                        unfocusedBorderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1A000000),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}

@Composable
private fun StyleToolPanel(
    element: TemplateElement?,
    onUpdateTextColor: (String) -> Unit,
    onUpdateBackgroundColor: (String?) -> Unit,
    onUpdateCornerRadius: (Float) -> Unit,
    onUpdateOpacity: (Float) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    if (element == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "یک المان را برای تغییر استایل و رنگ انتخاب کنید.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "رنگ متن یا خط المان:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Color Swatches (36dp with selection ring)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LUXURY_PALETTE.forEach { colorHex ->
                val isSelected = element.textColor.equals(colorHex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(colorHex)))
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) BrandPurple else (if (isDark) Color(0x33FFFFFF) else Color(0x22000000)),
                            shape = CircleShape
                        )
                        .clickable { onUpdateTextColor(colorHex) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Background Color Swatches for Pill / Shape
        Text(
            text = "رنگ پس‌زمینه المان:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Transparent option
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0x22FFFFFF) else Color(0x11000000))
                    .border(1.dp, if (isDark) Color(0x44FFFFFF) else Color(0x22000000), CircleShape)
                    .clickable { onUpdateBackgroundColor(null) },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            }

            LUXURY_PALETTE.forEach { colorHex ->
                val isSelected = element.backgroundColor?.equals(colorHex, ignoreCase = true) == true
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(colorHex)))
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) BrandCyan else (if (isDark) Color(0x33FFFFFF) else Color(0x22000000)),
                            shape = CircleShape
                        )
                        .clickable { onUpdateBackgroundColor(colorHex) }
                )
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}

@Composable
private fun BackgroundToolPanel(
    onSelectPreset: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "انتخاب گرادیانت پس‌زمینه قالب:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PRESET_BACKGROUNDS.forEach { preset ->
                val colors = preset.second.map { Color(android.graphics.Color.parseColor(it)) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSelectPreset(preset.second[0], preset.second[1]) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(colors))
                            .border(1.5.dp, Color(0x44FFFFFF), RoundedCornerShape(16.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = preset.first,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}

@Composable
private fun AddElementsToolPanel(
    onAddElement: (ElementType, String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AddElementButton("متن جدید", Icons.Rounded.Add) {
                onAddElement(ElementType.TEXT, "متن جدید شما")
            }
            AddElementButton("بلوک قیمت", Icons.Rounded.Add) {
                onAddElement(ElementType.PRICE_BLOCK, null)
            }
            AddElementButton("برچسب حراج", Icons.Rounded.Add) {
                onAddElement(ElementType.BADGE, "🔥 ۵۰٪ تخفیف ویژه")
            }
            AddElementButton("لوگوی برند", Icons.Rounded.Add) {
                onAddElement(ElementType.BRAND_LOGO, null)
            }
            AddElementButton("کادر دکوراتیو", Icons.Rounded.Add) {
                onAddElement(ElementType.SHAPE, null)
            }
            AddElementButton("تصویر محصول", Icons.Rounded.Add) {
                onAddElement(ElementType.IMAGE, null)
            }
            AddElementButton("فریم عکس", Icons.Rounded.Add) {
                onAddElement(ElementType.IMAGE_PLACEHOLDER, null)
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}

@Composable
private fun AddElementButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) DarkSurfaceCardElevated else Color(0xFFF1F5F9)),
        modifier = Modifier.height(48.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun LayersToolPanel(
    template: Template,
    selectedElementId: String?,
    onBringForward: (String) -> Unit,
    onSendBackward: (String) -> Unit,
    onDuplicate: (String) -> Unit,
    onDelete: (String) -> Unit,
    onSelectElement: (String?) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(235.dp)
    ) {
        Text(
            text = "لایه‌های قالب (کلیک برای انتخاب و مرتب‌سازی):",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(template.elements.reversed()) { _, el ->
                val isSelected = el.id == selectedElementId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0x338B5CF6) else (if (isDark) DarkSurfaceCard else Color(0xFFF1F5F9)))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) BrandPurple else (if (isDark) Color(0x15FFFFFF) else Color(0x15000000)),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectElement(el.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (el.type) {
                            ElementType.PRICE_BLOCK -> "بلوک قیمت و تخفیف"
                            ElementType.BRAND_LOGO -> "لوگوی برند"
                            else -> el.text ?: el.placeholderLabel ?: el.type.name
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) (if (isDark) Color.White else BrandPurple) else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onBringForward(el.id) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Rounded.ArrowUpward, contentDescription = "جلوتر", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { onSendBackward(el.id) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Rounded.ArrowDownward, contentDescription = "عقب‌تر", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { onDuplicate(el.id) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Rounded.ContentCopy, contentDescription = "تکثیر", tint = BrandCyan, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { onDelete(el.id) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Rounded.Delete, contentDescription = "حذف", tint = Color(0xFFF43F5E), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}

@Composable
private fun ImageToolPanel(
    element: TemplateElement?,
    isAdjustingInsideFrame: Boolean,
    onToggleAdjustInsideFrame: () -> Unit,
    onRequestPickImage: () -> Unit,
    onUpdateContentScale: (String) -> Unit,
    onUpdateCornerRadius: (Float) -> Unit,
    onUpdateImagePanZoom: (panX: Float, panY: Float, zoom: Float) -> Unit,
    onResetImagePanZoom: () -> Unit,
    onDeleteImage: (() -> Unit)? = null
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    if (element == null || (element.type != ElementType.IMAGE && element.type != ElementType.IMAGE_PLACEHOLDER)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "یک عکس یا قاب محصول را از روی بوم انتخاب کنید تا تنظیمات آن باز شود.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Gesture In-Frame Crop Button & Large Gallery / Delete Image Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Button to toggle in-frame pan & zoom gesture
            Button(
                onClick = onToggleAdjustInsideFrame,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAdjustingInsideFrame) Color(0xFFF59E0B) else (if (isDark) DarkSurfaceCardElevated else Color(0xFFF1F5F9))
                ),
                modifier = Modifier
                    .weight(1.1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = if (isAdjustingInsideFrame) Icons.Rounded.PanTool else Icons.Rounded.Crop,
                    contentDescription = null,
                    tint = if (isAdjustingInsideFrame) Color.Black else BrandCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAdjustingInsideFrame) "خروج از کادر" else "کادربندی قاب",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAdjustingInsideFrame) Color.Black else (if (isDark) Color.White else MaterialTheme.colorScheme.onSurface)
                )
            }

            // Large Gallery Pick Button with Gradient
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryGradientBrush)
                    .clickable { onRequestPickImage() }
                    .testTag("pick_image_gallery_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Rounded.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "انتخاب از گالری 📷",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Large Glass Delete Button
            if (onDeleteImage != null) {
                GlassSurface(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0x33EF4444),
                    borderColor = Color(0x66EF4444),
                    modifier = Modifier
                        .height(48.dp)
                        .clickable { onDeleteImage() }
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Delete, contentDescription = "حذف عکس", tint = Color(0xFFFCA5A5), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "حذف",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFCA5A5),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Scale mode toggle (CROP / FIT) & Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) DarkSurfaceCard else Color(0xFFF1F5F9))
                    .padding(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isCrop = element.contentScale != "FIT"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCrop) BrandCyan else Color.Transparent)
                        .clickable { onUpdateContentScale("CROP") }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "برش کامل",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        fontWeight = if (isCrop) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCrop) Color(0xFF0F172A) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isCrop) BrandCyan else Color.Transparent)
                        .clickable { onUpdateContentScale("FIT") }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "متناسب",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        fontWeight = if (!isCrop) FontWeight.Bold else FontWeight.Normal,
                        color = if (!isCrop) Color(0xFF0F172A) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "بازنشانی کادر ↺",
                style = MaterialTheme.typography.labelSmall,
                color = BrandPink,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onResetImagePanZoom() }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        // Zoom Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "بزرگ‌نمایی: ${(element.effectiveScale * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(115.dp)
            )
            Slider(
                value = element.effectiveScale,
                onValueChange = { newZoom ->
                    onUpdateImagePanZoom(element.effectiveOffsetX, element.effectiveOffsetY, newZoom)
                },
                valueRange = 0.8f..3.0f,
                colors = SliderDefaults.colors(
                    thumbColor = BrandCyan,
                    activeTrackColor = BrandPurple,
                    inactiveTrackColor = Color(0x338B5CF6)
                ),
                modifier = Modifier.weight(1f).height(24.dp)
            )
        }

        // Corner Radius Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "گردی لبه: ${element.cornerRadius.toInt()}dp",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(115.dp)
            )
            Slider(
                value = element.cornerRadius,
                onValueChange = onUpdateCornerRadius,
                valueRange = 0f..48f,
                colors = SliderDefaults.colors(
                    thumbColor = BrandPink,
                    activeTrackColor = BrandPink,
                    inactiveTrackColor = Color(0x33EC4899)
                ),
                modifier = Modifier.weight(1f).height(24.dp)
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding().height(16.dp))
    }
}
