package red.line.instayar.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand Aurora Gradients & Accents
val BrandViolet = Color(0xFF7C5CFF)
val BrandPink = Color(0xFFFF5CA8)
val BrandPurple = Color(0xFF7C5CFF)
val BrandMagenta = Color(0xFFFF5CA8)
val BrandCyan = Color(0xFF00E5FF)
val BrandTeal = Color(0xFF14B8A6)

// Gold / VIP Luxury Colors
val VipGold = Color(0xFFF5C451)
val VipGoldDark = Color(0xFFE0952B)
val VipGoldLight = Color(0xFFFDE68A)

// Dark Theme Luxury Backgrounds (تیره شفق + شیشه)
val DarkBackground = Color(0xFF0B0B10)
val DarkSurface = Color(0xFF12121A)
val DarkSurfaceCard = Color(0xFF181824)
val DarkSurfaceCardElevated = Color(0xFF222233)
val DarkBorder = Color(0x1EFFFFFF)
val DarkBorderSubtle = Color(0x14FFFFFF)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkTextMuted = Color(0xFF64748B)

// Fake Glass Colors (شیشه تقلبی)
val GlassSurfaceBg = Color(0x14FFFFFF) // آلفای ۸٪ سفید
val GlassBorderColor = Color(0x1EFFFFFF) // آلفای ۱۲٪ سفید
val GlassSurfaceHighlight = Color(0x22FFFFFF) // آلفای ۱۳٪ سفید

// Aurora Glow Colors for drawBehind
val AuroraGlowViolet = Color(0x477C5CFF) // بنفش ۲۸٪
val AuroraGlowPink = Color(0x33FF5CA8)   // صورتی ۲۰٪

// Light Theme (هماهنگ با سبک شفق)
val LightBackground = Color(0xFFF7F7FB)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceCard = Color(0xFFF0F0F6)
val LightSurfaceCardElevated = Color(0xFFE5E5F0)
val LightBorder = Color(0x1A7C5CFF)
val LightBorderSubtle = Color(0x0F000000)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
val LightTextMuted = Color(0xFF94A3B8)

// Gradients
val PrimaryGradientBrush = Brush.linearGradient(
    colors = listOf(BrandViolet, BrandPink)
)

val VipGradientBrush = Brush.linearGradient(
    colors = listOf(VipGold, VipGoldDark)
)

val CardGlowBrush = Brush.radialGradient(
    colors = listOf(Color(0x337C5CFF), Color.Transparent),
    radius = 500f
)
