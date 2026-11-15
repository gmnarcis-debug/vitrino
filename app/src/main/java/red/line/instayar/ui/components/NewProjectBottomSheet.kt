package red.line.instayar.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CropPortrait
import androidx.compose.material.icons.rounded.CropSquare
import androidx.compose.material.icons.rounded.PanoramaVertical
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.instayar.data.model.TemplateFormat
import red.line.instayar.ui.theme.BrandPink
import red.line.instayar.ui.theme.BrandPurple
import red.line.instayar.ui.theme.BrandViolet
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.DarkBorder
import red.line.instayar.ui.theme.DarkSurface
import red.line.instayar.ui.theme.DarkSurfaceCard
import red.line.instayar.ui.theme.PrimaryGradientBrush

data class FormatOption(
    val format: TemplateFormat,
    val title: String,
    val subtitle: String,
    val ratioLabel: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectBottomSheet(
    onDismiss: () -> Unit,
    onCreateFormatSelected: (TemplateFormat) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val formats = listOf(
        FormatOption(
            format = TemplateFormat.STORY,
            title = "استوری اینستاگرام",
            subtitle = "طراحی تمام‌صفحه عمودی",
            ratioLabel = "۹:۱۶",
            icon = Icons.Rounded.CropPortrait
        ),
        FormatOption(
            format = TemplateFormat.POST_SQUARE,
            title = "پست مربعی",
            subtitle = "مناسب فید و اسلایدری",
            ratioLabel = "۱:۱",
            icon = Icons.Rounded.CropSquare
        ),
        FormatOption(
            format = TemplateFormat.POST_PORTRAIT,
            title = "پست عمودی",
            subtitle = "بیشترین دیده‌شدن در فید",
            ratioLabel = "۴:۵",
            icon = Icons.Rounded.PanoramaVertical
        ),
        FormatOption(
            format = TemplateFormat.HIGHLIGHT,
            title = "کاور هایلایت",
            subtitle = "آیکون و متن دایره‌ای",
            ratioLabel = "۱:۱",
            icon = Icons.Rounded.AutoAwesome
        )
    )

    var selectedFormat by remember { mutableStateOf(TemplateFormat.STORY) }

    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) DarkSurface else Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0x33FFFFFF) else Color(0x33000000))
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            // هدر باتم‌شیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ساخت طرح جدید ✨",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "اندازه و ابعاد بوم طراحی را انتخاب کنید",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ردیف ۲x۲ گزینه‌های اندازه
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                formats.forEach { option ->
                    val isSelected = selectedFormat == option.format

                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) BrandPink else if (isDark) Color(0x1EFFFFFF) else Color(0x1E000000),
                        label = "borderColor"
                    )
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            if (isDark) Color(0x227C5CFF) else Color(0x1A7C5CFF)
                        } else {
                            if (isDark) Color(0x0EFFFFFF) else Color(0xF2F8FAFC)
                        },
                        label = "bgColor"
                    )

                    GlassSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedFormat = option.format
                            },
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = bgColor,
                        borderColor = borderColor,
                        borderWidth = if (isSelected) 1.5.dp else 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                val iconBgModifier = if (isSelected) {
                                    Modifier.background(PrimaryGradientBrush)
                                } else {
                                    Modifier.background(if (isDark) Color(0x1FFFFFFF) else Color(0x0A000000))
                                }

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .then(iconBgModifier),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = option.icon,
                                        contentDescription = option.title,
                                        tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = option.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = option.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // برچسب نسبت
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDark) Color(0x1AFFFFFF) else Color(0x0A000000))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = option.ratioLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BrandPink else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // دکمه گرادیانتی ایجاد طرح
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryGradientBrush)
                    .clickable {
                        onCreateFormatSelected(selectedFormat)
                        onDismiss()
                    }
                    .testTag("create_blank_project_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "ایجاد طرح جدید",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
