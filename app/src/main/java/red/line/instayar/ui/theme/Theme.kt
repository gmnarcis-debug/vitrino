package red.line.instayar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = BrandViolet,
    onPrimary = Color.White,
    primaryContainer = BrandViolet,
    onPrimaryContainer = Color.White,
    secondary = BrandPink,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceCardElevated,
    onSecondaryContainer = BrandPink,
    tertiary = VipGold,
    onTertiary = DarkBackground,
    tertiaryContainer = VipGoldDark,
    onTertiaryContainer = VipGoldLight,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorderSubtle
)

private val LightColorScheme = lightColorScheme(
    primary = BrandViolet,
    onPrimary = Color.White,
    primaryContainer = BrandPink,
    onPrimaryContainer = Color.White,
    secondary = BrandViolet,
    onSecondary = Color.White,
    secondaryContainer = LightSurfaceCardElevated,
    onSecondaryContainer = LightTextPrimary,
    tertiary = VipGold,
    onTertiary = Color.White,
    tertiaryContainer = VipGoldLight,
    onTertiaryContainer = DarkBackground,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = LightBorderSubtle
)

// گوشه‌ها ۲۴dp برای کارت‌ها، ۱۶dp برای دکمه‌ها و چیپ‌ها، pill کامل برای نوارها
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun VitrinoTheme(
    darkTheme: Boolean = true, // تیره به عنوان پیش‌فرض لوکس
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

@Composable
fun GhalibinoTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    VitrinoTheme(darkTheme = darkTheme, content = content)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    VitrinoTheme(darkTheme = darkTheme, content = content)
}
