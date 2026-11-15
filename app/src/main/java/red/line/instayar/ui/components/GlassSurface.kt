package red.line.instayar.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import red.line.instayar.ui.theme.DarkBackground
import red.line.instayar.ui.theme.GlassBorderColor
import red.line.instayar.ui.theme.GlassSurfaceBg

/**
 * سطح شیشه‌ای (Fake Glass Surface):
 * در تم تیره: سطح نیمه‌شفاف با حاشیه ظریف سفید.
 * در تم روشن: سطح سفید تمیز و شفاف با حاشیه ملایم برند و کنتراست عالی.
 * بدون استفاده از Blur یا RenderEffect تا روی تمامی دستگاه‌ها با حداکثر پرفورمنس اجرا شود.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground
    val defaultBg = if (isDark) GlassSurfaceBg else Color(0xF2FFFFFF)
    val defaultBorder = if (isDark) GlassBorderColor else Color(0x1A7C5CFF)

    val actualBg = if (backgroundColor != Color.Unspecified) backgroundColor else defaultBg
    val actualBorder = if (borderColor != Color.Unspecified) borderColor else defaultBorder

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(actualBg, shape)
            .border(borderWidth, actualBorder, shape)
            .then(clickModifier)
    ) {
        val contentColor = if (isDark) Color.White else Color(0xFF0F172A)
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            content()
        }
    }
}

/**
 * پس‌زمینه پویا با درخشش شفق نرم:
 * - در تم تیره: پس‌زمینه #0B0B10 با شفق بنفش و صورتی عمیق
 * - در تم روشن: پس‌زمینه #F7F7FB با شفق ملایم و دلنشین
 */
fun Modifier.auroraGlowBackground(): Modifier = this.composed {
    val bgColor = MaterialTheme.colorScheme.background
    val isDark = bgColor == DarkBackground

    this.drawBehind {
        drawRect(bgColor)

        if (isDark) {
            // درخشش شفق بنفش در بالا-راست
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x477C5CFF), Color(0x187C5CFF), Color.Transparent),
                    center = Offset(x = size.width * 0.95f, y = size.height * 0.08f),
                    radius = size.width * 0.95f
                ),
                radius = size.width * 0.95f,
                center = Offset(x = size.width * 0.95f, y = size.height * 0.08f)
            )

            // درخشش شفق صورتی در بالا-چپ
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x33FF5CA8), Color(0x10FF5CA8), Color.Transparent),
                    center = Offset(x = size.width * 0.05f, y = size.height * 0.05f),
                    radius = size.width * 0.85f
                ),
                radius = size.width * 0.85f,
                center = Offset(x = size.width * 0.05f, y = size.height * 0.05f)
            )
        } else {
            // درخشش شفق تم روشن
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x1E7C5CFF), Color(0x067C5CFF), Color.Transparent),
                    center = Offset(x = size.width * 0.95f, y = size.height * 0.08f),
                    radius = size.width * 0.95f
                ),
                radius = size.width * 0.95f,
                center = Offset(x = size.width * 0.95f, y = size.height * 0.08f)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x18FF5CA8), Color(0x04FF5CA8), Color.Transparent),
                    center = Offset(x = size.width * 0.05f, y = size.height * 0.05f),
                    radius = size.width * 0.85f
                ),
                radius = size.width * 0.85f,
                center = Offset(x = size.width * 0.05f, y = size.height * 0.05f)
            )
        }
    }
}

/**
 * افکت شیمر نرم (Shimmer Skeleton) برای اسکلت لودینگ بدون وابستگی خارجی
 */
@Composable
fun shimmerBrush(
    targetValue: Float = 1000f,
    showShimmer: Boolean = true
): Brush {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground
    return if (showShimmer) {
        val shimmerColors = if (isDark) {
            listOf(
                Color(0x10FFFFFF),
                Color(0x28FFFFFF),
                Color(0x10FFFFFF)
            )
        } else {
            listOf(
                Color(0x147C5CFF),
                Color(0x337C5CFF),
                Color(0x147C5CFF)
            )
        }

        val transition = rememberInfiniteTransition(label = "shimmerTransition")
        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmerTranslate"
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation - targetValue, translateAnimation - targetValue),
            end = Offset(translateAnimation, translateAnimation)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}
