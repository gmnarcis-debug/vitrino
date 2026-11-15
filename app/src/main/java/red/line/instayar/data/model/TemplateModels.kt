package red.line.instayar.data.model

data class Template(
    val id: String,
    val title: String,
    val category: String,
    val format: TemplateFormat,
    val isVip: Boolean,
    val tags: List<String> = emptyList(),
    val background: TemplateBackground,
    val elements: List<TemplateElement> = emptyList()
) {
    val aspectRatioFloat: Float
        get() = when (format) {
            TemplateFormat.STORY -> 9f / 16f
            TemplateFormat.POST_PORTRAIT -> 4f / 5f
            TemplateFormat.POST_SQUARE -> 1f
            TemplateFormat.HIGHLIGHT -> 1f
        }
}

enum class TemplateFormat(val titleFa: String, val ratioWidth: Int, val ratioHeight: Int) {
    STORY("استوری", 9, 16),
    POST_PORTRAIT("پست عمودی", 4, 5),
    POST_SQUARE("پست مربعی", 1, 1),
    HIGHLIGHT("کاور هایلایت", 1, 1);

    companion object {
        fun fromString(value: String): TemplateFormat {
            return when (value.uppercase()) {
                "STORY" -> STORY
                "POST_PORTRAIT" -> POST_PORTRAIT
                "POST_SQUARE", "POST" -> POST_SQUARE
                "HIGHLIGHT" -> HIGHLIGHT
                else -> STORY
            }
        }
    }
}

data class TemplateBackground(
    val type: String = "GRADIENT", // SOLID, GRADIENT, IMAGE
    val color: String = "#1A102F",
    val gradientColors: List<String> = listOf("#2E0854", "#0C071E"),
    val angle: Float = 45f,
    val imageAsset: String? = null,
    val overlayColors: List<String> = listOf("#80000000", "#D0000000"),
    val overlayAlpha: Float = 0.6f
)

data class TemplateElement(
    val id: String,
    val type: ElementType,
    val x: Float, // 0.0f - 1.0f relative to canvas width
    val y: Float, // 0.0f - 1.0f relative to canvas height
    val width: Float, // 0.0f - 1.0f
    val height: Float, // 0.0f - 1.0f
    val rotation: Float = 0f,
    val text: String? = null,
    val fontSize: Float = 16f,
    val fontWeight: String = "BOLD", // NORMAL, MEDIUM, BOLD, BLACK
    val textColor: String = "#FFFFFF",
    val textAlign: String = "CENTER", // CENTER, RIGHT, LEFT
    val backgroundColor: String? = null,
    val shapeType: String = "ROUNDED_RECT", // RECTANGLE, ROUNDED_RECT, CIRCLE, TAG, RIBBON
    val cornerRadius: Float = 16f,
    val borderColor: String? = null,
    val borderWidth: Float = 0f,
    val badgeIcon: String? = null, // DISCOUNT, STAR, CROWN, FIRE, CHECK
    val placeholderLabel: String? = null,
    val hasShadow: Boolean = false,
    val shadowColor: String = "#80000000",
    val imageAsset: String? = null, // filename in assets/images/, e.g. "fashion_01.webp"
    val contentScale: String = "CROP", // "CROP" or "FIT"
    val imageUri: String? = null, // Custom user replaced photo from gallery
    val imagePanX: Float = 0f, // -1f .. 1f
    val imagePanY: Float = 0f, // -1f .. 1f
    val imageZoom: Float = 1f,  // 1f .. 3f

    // 1. New in-frame image pan & zoom fields
    val imageScale: Float = 1f,
    val imageOffsetX: Float = 0f,
    val imageOffsetY: Float = 0f,

    // 3. New Text typography and styling fields
    val fontFamily: String? = "vazirmatn", // vazirmatn, sahel, samim, shabnam, tanha, gandom, parastoo, lalezar
    val outlineColor: String? = null,
    val outlineWidth: Float = 0f,
    val opacity: Float = 1f,
    val letterSpacing: Float = 0f,

    // 5. New Price Block fields
    val oldPrice: Long? = null,
    val newPrice: Long? = null,
    val currencyUnit: String = "تومان"
) {
    val effectiveScale: Float
        get() = if (imageScale != 1f) imageScale else imageZoom

    val effectiveOffsetX: Float
        get() = if (imageOffsetX != 0f) imageOffsetX else imagePanX

    val effectiveOffsetY: Float
        get() = if (imageOffsetY != 0f) imageOffsetY else imagePanY

    val discountPercentage: Int
        get() {
            val oldP = oldPrice ?: return 0
            val newP = newPrice ?: return 0
            return if (oldP > newP && oldP > 0) {
                (((oldP - newP).toDouble() / oldP.toDouble()) * 100.0).toInt().coerceIn(1, 99)
            } else 0
        }
}

enum class ElementType {
    TEXT,
    IMAGE_PLACEHOLDER,
    IMAGE,
    SHAPE,
    BADGE,
    STICKER,
    BRAND_LOGO,
    BRAND_NAME,
    BRAND_CONTACT,
    PRICE_BLOCK;

    companion object {
        fun fromString(value: String): ElementType {
            return when (value.uppercase()) {
                "TEXT" -> TEXT
                "IMAGE_PLACEHOLDER" -> IMAGE_PLACEHOLDER
                "IMAGE" -> IMAGE
                "SHAPE" -> SHAPE
                "BADGE" -> BADGE
                "STICKER" -> STICKER
                "BRAND_LOGO" -> BRAND_LOGO
                "BRAND_NAME" -> BRAND_NAME
                "BRAND_CONTACT" -> BRAND_CONTACT
                "PRICE_BLOCK" -> PRICE_BLOCK
                else -> TEXT
            }
        }
    }
}

data class BrandProfile(
    val brandName: String = "فروشگاه آنلاین من",
    val instagramHandle: String = "@my_shop_page",
    val phoneNumber: String = "۰۹۱۲۳۴۵۶۷۸۹",
    val websiteOrAddress: String = "تهران، بازار بزرگ",
    val primaryColorHex: String = "#7C3AED",
    val secondaryColorHex: String = "#EC4899",
    val logoUri: String? = null
)
