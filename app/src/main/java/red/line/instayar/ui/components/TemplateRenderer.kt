package red.line.instayar.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import red.line.instayar.R
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.data.model.ElementType
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateBackground
import red.line.instayar.data.model.TemplateElement
import red.line.instayar.util.AssetImageLoader
import red.line.instayar.util.PersianDateUtil
import java.io.File
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Memory cache for user brand logos and local custom images to prevent decoding on every frame
private val brandLogoLruCache = object : android.util.LruCache<String, Bitmap>(10) {
    override fun sizeOf(key: String, value: Bitmap): Int {
        return value.byteCount / 1024
    }
}

@Composable
fun TemplateThumbnailPreview(
    template: Template,
    modifier: Modifier = Modifier,
    brandProfile: BrandProfile = BrandProfile(),
    cornerRadius: Float = 20f
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .aspectRatio(template.aspectRatioFloat)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(Color(0xFF1E1935))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawTemplate(
                template = template,
                canvasSize = size,
                brandProfile = brandProfile,
                context = context
            )
        }
    }
}

fun DrawScope.drawTemplate(
    template: Template,
    canvasSize: Size,
    brandProfile: BrandProfile = BrandProfile(),
    context: Context? = null
) {
    val w = canvasSize.width
    val h = canvasSize.height

    // 1. Draw Background
    drawTemplateBackground(template.background, w, h, context)

    // 2. Draw Elements in order
    for (element in template.elements) {
        drawTemplateElement(element, w, h, brandProfile, context)
    }
}

private fun DrawScope.drawTemplateBackground(
    bg: TemplateBackground,
    w: Float,
    h: Float,
    context: Context? = null
) {
    if (bg.type.uppercase() == "IMAGE" && !bg.imageAsset.isNullOrBlank() && context != null) {
        val bgBitmap = AssetImageLoader.loadBitmap(context, bg.imageAsset, null, w.toInt(), h.toInt())
        if (bgBitmap != null) {
            val bmpW = bgBitmap.width.toFloat()
            val bmpH = bgBitmap.height.toFloat()
            val viewAspect = w / h
            val bmpAspect = bmpW / bmpH
            val (cropW, cropH) = if (bmpAspect > viewAspect) {
                Pair(bmpH * viewAspect, bmpH)
            } else {
                Pair(bmpW, bmpW / viewAspect)
            }
            val srcLeft = (bmpW - cropW) / 2f
            val srcTop = (bmpH - cropH) / 2f
            val srcRect = Rect(srcLeft.toInt(), srcTop.toInt(), (srcLeft + cropW).toInt(), (srcTop + cropH).toInt())
            val dstRect = RectF(0f, 0f, w, h)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            drawContext.canvas.nativeCanvas.drawBitmap(bgBitmap, srcRect, dstRect, paint)

            val overlayColors = if (bg.overlayColors.isNotEmpty()) {
                bg.overlayColors.map { parseColorHex(it).copy(alpha = bg.overlayAlpha) }
            } else {
                listOf(Color.Black.copy(alpha = bg.overlayAlpha), Color.Black.copy(alpha = bg.overlayAlpha))
            }
            drawRect(
                brush = Brush.verticalGradient(
                    colors = if (overlayColors.size > 1) overlayColors else listOf(overlayColors.first(), overlayColors.first()),
                    startY = 0f,
                    endY = h
                ),
                size = Size(w, h)
            )
            return
        }
    }

    if (bg.type.uppercase() == "GRADIENT" && bg.gradientColors.isNotEmpty()) {
        val colors = bg.gradientColors.map { parseColorHex(it) }
        val rad = (bg.angle * PI / 180f).toFloat()
        val startOffset = Offset(0f, 0f)
        val endOffset = Offset(
            x = (w * cos(rad)).coerceIn(0f, w),
            y = (h * sin(rad)).coerceIn(0f, h)
        )
        drawRect(
            brush = Brush.linearGradient(
                colors = if (colors.size > 1) colors else listOf(colors.first(), colors.first()),
                start = startOffset,
                end = if (endOffset == Offset.Zero) Offset(w, h) else endOffset
            ),
            size = Size(w, h)
        )
    } else {
        drawRect(
            color = parseColorHex(bg.color),
            size = Size(w, h)
        )
    }

    if (bg.type.uppercase() == "GRADIENT") {
        // Add subtle ambient glowing circles for depth and polish on gradient backgrounds
        drawCircle(
            color = Color(0x18FFFFFF),
            radius = w * 0.45f,
            center = Offset(w * 0.85f, h * 0.15f)
        )
        drawCircle(
            color = Color(0x12FFFFFF),
            radius = w * 0.35f,
            center = Offset(w * 0.15f, h * 0.85f)
        )
    }
}

private fun DrawScope.drawTemplateElement(
    element: TemplateElement,
    canvasW: Float,
    canvasH: Float,
    brandProfile: BrandProfile,
    context: Context? = null
) {
    val elX = element.x * canvasW
    val elY = element.y * canvasH
    val elW = element.width * canvasW
    val elH = element.height * canvasH
    val centerX = elX + elW / 2f
    val centerY = elY + elH / 2f

    rotate(degrees = element.rotation, pivot = Offset(centerX, centerY)) {
        when (element.type) {
            ElementType.SHAPE -> {
                drawShapeElement(element, elX, elY, elW, elH)
            }
            ElementType.IMAGE, ElementType.IMAGE_PLACEHOLDER -> {
                val hasRealImage = !element.imageUri.isNullOrBlank() || !element.imageAsset.isNullOrBlank()
                var drawnRealImage = false
                if (hasRealImage && context != null) {
                    val bitmap = AssetImageLoader.loadBitmap(
                        context = context,
                        assetName = element.imageAsset,
                        imageUri = element.imageUri,
                        targetWidth = elW.toInt(),
                        targetHeight = elH.toInt()
                    )
                    if (bitmap != null) {
                        drawRealImageElement(element, bitmap, elX, elY, elW, elH)
                        drawnRealImage = true
                    }
                }
                if (!drawnRealImage) {
                    drawImagePlaceholder(element, elX, elY, elW, elH)
                }
            }
            ElementType.TEXT -> {
                drawTextElement(element, element.text, elX, elY, elW, elH, canvasW, context)
            }
            ElementType.BRAND_NAME -> {
                val brandText = if (brandProfile.brandName.isNotBlank()) brandProfile.brandName else (element.text ?: "")
                drawTextElement(element, brandText, elX, elY, elW, elH, canvasW, context)
            }
            ElementType.BRAND_CONTACT -> {
                val contactText = when {
                    brandProfile.phoneNumber.isNotBlank() && brandProfile.instagramHandle.isNotBlank() ->
                        "${brandProfile.instagramHandle} | ${brandProfile.phoneNumber}"
                    brandProfile.phoneNumber.isNotBlank() -> brandProfile.phoneNumber
                    brandProfile.instagramHandle.isNotBlank() -> brandProfile.instagramHandle
                    else -> element.text ?: ""
                }
                drawTextElement(element, contactText, elX, elY, elW, elH, canvasW, context)
            }
            ElementType.BADGE, ElementType.STICKER -> {
                drawBadgeElement(element, elX, elY, elW, elH, canvasW, context)
            }
            ElementType.BRAND_LOGO -> {
                drawBrandLogoElement(element, elX, elY, elW, elH, brandProfile, context)
            }
            ElementType.PRICE_BLOCK -> {
                drawPriceBlockElement(element, elX, elY, elW, elH, canvasW, context)
            }
        }
    }
}

private fun buildShapePath(
    shapeType: String,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    corner: Float
): Path {
    return Path().apply {
        when (shapeType.uppercase()) {
            "CIRCLE", "OVAL" -> {
                addOval(androidx.compose.ui.geometry.Rect(x, y, x + w, y + h))
            }
            "ARCH" -> {
                val archCorner = CornerRadius(w / 2f, w / 2f)
                val bottomCorner = CornerRadius(corner.coerceAtMost(24f), corner.coerceAtMost(24f))
                addRoundRect(
                    RoundRect(
                        left = x,
                        top = y,
                        right = x + w,
                        bottom = y + h,
                        topLeftCornerRadius = archCorner,
                        topRightCornerRadius = archCorner,
                        bottomLeftCornerRadius = bottomCorner,
                        bottomRightCornerRadius = bottomCorner
                    )
                )
            }
            "PARALLELOGRAM" -> {
                val slant = w * 0.16f
                moveTo(x + slant, y)
                lineTo(x + w, y)
                lineTo(x + w - slant, y + h)
                lineTo(x, y + h)
                close()
            }
            "HEXAGON" -> {
                moveTo(x + w * 0.5f, y)
                lineTo(x + w, y + h * 0.25f)
                lineTo(x + w, y + h * 0.75f)
                lineTo(x + w * 0.5f, y + h)
                lineTo(x, y + h * 0.75f)
                lineTo(x, y + h * 0.25f)
                close()
            }
            "TAG" -> {
                moveTo(x + 16f, y)
                lineTo(x + w, y)
                lineTo(x + w, y + h)
                lineTo(x + 16f, y + h)
                lineTo(x, y + h / 2f)
                close()
            }
            "RIBBON" -> {
                moveTo(x, y)
                lineTo(x + w, y)
                lineTo(x + w - 12f, y + h / 2f)
                lineTo(x + w, y + h)
                lineTo(x, y + h)
                lineTo(x + 12f, y + h / 2f)
                close()
            }
            else -> {
                addRoundRect(
                    RoundRect(
                        left = x,
                        top = y,
                        right = x + w,
                        bottom = y + h,
                        cornerRadius = CornerRadius(corner, corner)
                    )
                )
            }
        }
    }
}

private fun DrawScope.drawShapeElement(element: TemplateElement, x: Float, y: Float, w: Float, h: Float) {
    val elOpacity = element.opacity.coerceIn(0f, 1f)
    val bgColor = element.backgroundColor?.let { parseColorHex(it).copy(alpha = it.let { hex -> parseColorHex(hex).alpha * elOpacity }) }
        ?: Color(0x33FFFFFF).copy(alpha = 0.2f * elOpacity)
    val corner = element.cornerRadius * (w / 300f).coerceAtLeast(0.5f)

    if (element.shapeType.uppercase() == "RING") {
        val ringColor = (element.borderColor ?: element.backgroundColor)?.let { parseColorHex(it) } ?: Color(0x88FFFFFF)
        val strokeW = if (element.borderWidth > 0f) element.borderWidth else 2.5f
        drawCircle(
            color = ringColor.copy(alpha = ringColor.alpha * elOpacity),
            radius = minOf(w, h) / 2f,
            center = Offset(x + w / 2f, y + h / 2f),
            style = Stroke(width = strokeW)
        )
        return
    }

    val path = buildShapePath(element.shapeType, x, y, w, h, corner)
    drawPath(path = path, color = bgColor, style = Fill)

    if (element.borderWidth > 0f && element.borderColor != null) {
        val bColor = parseColorHex(element.borderColor).copy(alpha = elOpacity)
        drawPath(path = path, color = bColor, style = Stroke(width = element.borderWidth))
    }
}

private fun DrawScope.drawRealImageElement(
    element: TemplateElement,
    bitmap: Bitmap,
    x: Float,
    y: Float,
    w: Float,
    h: Float
) {
    val corner = (element.cornerRadius * (w / 300f)).coerceAtLeast(0f)
    val elOpacity = element.opacity.coerceIn(0f, 1f)
    val shapePath = buildShapePath(element.shapeType, x, y, w, h, corner)

    // 1. Drop shadow if enabled
    if (element.hasShadow) {
        val shadowCol = parseColorHex(element.shadowColor).copy(alpha = 0.5f * elOpacity)
        val shadowPath = buildShapePath(element.shapeType, x + 4f, y + 6f, w, h, corner)
        drawPath(path = shadowPath, color = shadowCol, style = Fill)
    }

    clipPath(shapePath) {
        // Background fill if specified
        if (element.backgroundColor != null) {
            drawRect(
                color = parseColorHex(element.backgroundColor).copy(alpha = elOpacity),
                topLeft = Offset(x, y),
                size = Size(w, h)
            )
        }

        val bmpW = bitmap.width.toFloat()
        val bmpH = bitmap.height.toFloat()

        val scaleFactor = element.effectiveScale.coerceIn(0.5f, 5f)
        val offsetRelX = element.effectiveOffsetX.coerceIn(-1.5f, 1.5f)
        val offsetRelY = element.effectiveOffsetY.coerceIn(-1.5f, 1.5f)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
        }

        if (element.contentScale.uppercase() == "FIT") {
            // FIT mode: preserve aspect ratio, fit inside bounding box, then zoom & offset
            val baseScale = minOf(w / bmpW, h / bmpH) * scaleFactor
            val destW = bmpW * baseScale
            val destH = bmpH * baseScale
            val maxPanX = w / 2f
            val maxPanY = h / 2f
            val destX = x + (w - destW) / 2f + (offsetRelX * maxPanX)
            val destY = y + (h - destH) / 2f + (offsetRelY * maxPanY)

            val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
            val dstRect = RectF(destX, destY, destX + destW, destY + destH)
            drawContext.canvas.nativeCanvas.drawBitmap(bitmap, srcRect, dstRect, paint)
        } else {
            // CROP mode: fill entire element rect, cropping according to zoom & offset
            val viewAspect = w / h
            val bmpAspect = bmpW / bmpH

            val (baseCropW, baseCropH) = if (bmpAspect > viewAspect) {
                Pair(bmpH * viewAspect, bmpH)
            } else {
                Pair(bmpW, bmpW / viewAspect)
            }

            val cropW = (baseCropW / scaleFactor).coerceIn(10f, bmpW)
            val cropH = (baseCropH / scaleFactor).coerceIn(10f, bmpH)

            val maxPanX = (bmpW - cropW) / 2f
            val maxPanY = (bmpH - cropH) / 2f
            val centerX = (bmpW / 2f) - (offsetRelX * maxPanX)
            val centerY = (bmpH / 2f) - (offsetRelY * maxPanY)

            val srcLeft = (centerX - cropW / 2f).coerceIn(0f, bmpW - cropW)
            val srcTop = (centerY - cropH / 2f).coerceIn(0f, bmpH - cropH)

            val srcRect = Rect(
                srcLeft.toInt(),
                srcTop.toInt(),
                (srcLeft + cropW).toInt(),
                (srcTop + cropH).toInt()
            )
            val dstRect = RectF(x, y, x + w, y + h)
            drawContext.canvas.nativeCanvas.drawBitmap(bitmap, srcRect, dstRect, paint)
        }

        // Indicator pill if placeholder is not yet replaced
        if (element.type == ElementType.IMAGE_PLACEHOLDER && element.imageUri == null) {
            val badgeH = (h * 0.12f).coerceIn(18f, 30f)
            val badgeW = (w * 0.55f).coerceIn(80f, 150f)
            val badgeY = y + h - badgeH - 10f
            val badgeX = x + (w - badgeW) / 2f

            drawRoundRect(
                color = Color(0xCC0F0B1E),
                topLeft = Offset(badgeX, badgeY),
                size = Size(badgeW, badgeH),
                cornerRadius = CornerRadius(badgeH / 2f, badgeH / 2f)
            )
            drawRoundRect(
                color = Color(0x55FFFFFF),
                topLeft = Offset(badgeX, badgeY),
                size = Size(badgeW, badgeH),
                cornerRadius = CornerRadius(badgeH / 2f, badgeH / 2f),
                style = Stroke(width = 1f)
            )

            drawContext.canvas.nativeCanvas.apply {
                val p = Paint().apply {
                    color = AndroidColor.WHITE
                    textSize = (badgeH * 0.46f).coerceIn(10f, 13f)
                    typeface = Typeface.DEFAULT_BOLD
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                val tY = badgeY + badgeH / 2f - (p.descent() + p.ascent()) / 2f
                drawText("عکس نمونه (لمس برای تغییر)", badgeX + badgeW / 2f, tY, p)
            }
        }
    }

    // 3. Border if specified
    if (element.borderWidth > 0f && element.borderColor != null) {
        val bColor = parseColorHex(element.borderColor).copy(alpha = elOpacity)
        drawPath(path = shapePath, color = bColor, style = Stroke(width = element.borderWidth))
    }
}

private fun DrawScope.drawImagePlaceholder(element: TemplateElement, x: Float, y: Float, w: Float, h: Float) {
    val corner = (element.cornerRadius * (w / 300f)).coerceAtLeast(8f)
    val cx = x + w / 2f
    val cy = y + h / 2f

    val baseColor = element.backgroundColor?.let { parseColorHex(it) } ?: Color(0xFF2E2452)
    val lightTop = Color(0xFF4C3882)
    val darkBottom = Color(0xFF191330)
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(lightTop, baseColor, darkBottom),
            startY = y,
            endY = y + h
        ),
        topLeft = Offset(x, y),
        size = Size(w, h),
        cornerRadius = CornerRadius(corner, corner)
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x38FFFFFF), Color(0x00FFFFFF)),
            center = Offset(cx, cy - h * 0.08f),
            radius = minOf(w, h) * 0.45f
        ),
        radius = minOf(w, h) * 0.45f,
        center = Offset(cx, cy - h * 0.08f)
    )

    val strokeColor = element.borderColor?.let { parseColorHex(it) } ?: Color(0x88A78BFA)
    drawRoundRect(
        color = strokeColor,
        topLeft = Offset(x, y),
        size = Size(w, h),
        cornerRadius = CornerRadius(corner, corner),
        style = Stroke(width = 2.5f)
    )

    val pillH = (h * 0.11f).coerceIn(16f, 28f)
    val pillW = (w * 0.65f).coerceIn(60f, 150f)
    val pillY = cy + h * 0.36f

    drawRoundRect(
        color = Color(0xD91E1935),
        topLeft = Offset(cx - pillW / 2f, pillY),
        size = Size(pillW, pillH),
        cornerRadius = CornerRadius(pillH / 2f, pillH / 2f)
    )

    drawContext.canvas.nativeCanvas.apply {
        val paint = Paint().apply {
            color = AndroidColor.WHITE
            textSize = (pillH * 0.55f).coerceIn(9f, 14f)
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val textY = pillY + pillH / 2f - (paint.descent() + paint.ascent()) / 2f
        drawText(element.placeholderLabel ?: "جایگاه تصویر محصول", cx, textY, paint)
    }
}

private fun resolveTypeface(context: Context?, fontFamilyName: String?, isBold: Boolean): Typeface {
    if (context == null || fontFamilyName.isNullOrBlank()) {
        return if (isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }
    val cleanName = fontFamilyName.lowercase().trim()
    val resId = when (cleanName) {
        "vazirmatn" -> R.font.vazirmatn
        "sahel" -> R.font.sahel
        "samim" -> R.font.samim
        "shabnam" -> R.font.shabnam
        "tanha" -> R.font.tanha
        "gandom" -> R.font.gandom
        "parastoo" -> R.font.parastoo
        "lalezar" -> R.font.lalezar
        else -> R.font.vazirmatn
    }
    return try {
        val tf = ResourcesCompat.getFont(context, resId)
        tf ?: if (isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    } catch (e: Exception) {
        if (isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }
}

private fun DrawScope.drawTextElement(
    element: TemplateElement,
    rawText: String?,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    canvasW: Float,
    context: Context? = null
) {
    val text = rawText ?: return
    if (text.isBlank()) return

    val elOpacity = element.opacity.coerceIn(0f, 1f)

    if (element.backgroundColor != null) {
        val pillColor = parseColorHex(element.backgroundColor).copy(alpha = elOpacity)
        val corner = element.cornerRadius * (w / 300f).coerceAtLeast(0.5f)
        drawRoundRect(
            color = pillColor,
            topLeft = Offset(x, y),
            size = Size(w, h),
            cornerRadius = CornerRadius(corner, corner)
        )
    }

    drawIntoCanvas { canvas ->
        val scale = canvasW / 360f
        val isBold = element.fontWeight.uppercase() in listOf("BOLD", "BLACK", "EXTRA_BOLD")
        val customTypeface = resolveTypeface(context, element.fontFamily, isBold)

        val alignment = when (element.textAlign.uppercase()) {
            "RIGHT" -> Layout.Alignment.ALIGN_OPPOSITE
            "LEFT" -> Layout.Alignment.ALIGN_NORMAL
            else -> Layout.Alignment.ALIGN_CENTER
        }
        val availableWidth = (w - 8f).toInt().coerceAtLeast(10)

        // 1. Draw Text Outline if outlineWidth > 0 and outlineColor is set
        if (element.outlineWidth > 0f && !element.outlineColor.isNullOrBlank()) {
            val strokePaint = TextPaint().apply {
                val outColor = AndroidColor.parseColor(sanitizeHex(element.outlineColor))
                color = outColor
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
                textSize = (element.fontSize * scale).coerceAtLeast(9f)
                typeface = customTypeface
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = (element.outlineWidth * scale * 1.5f).coerceIn(1f, 16f)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && element.letterSpacing != 0f) {
                    letterSpacing = element.letterSpacing
                }
            }
            val strokeLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(text, 0, text.length, strokePaint, availableWidth)
                    .setAlignment(alignment)
                    .setLineSpacing(2f, 1f)
                    .setIncludePad(false)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(text, strokePaint, availableWidth, alignment, 1.0f, 2.0f, false)
            }
            val layoutH = strokeLayout.height
            val startY = y + (h - layoutH) / 2f
            val startX = x + 4f
            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(startX, startY)
            strokeLayout.draw(canvas.nativeCanvas)
            canvas.nativeCanvas.restore()
        }

        // 2. Draw Text Fill
        val textPaint = TextPaint().apply {
            val parsedCol = AndroidColor.parseColor(sanitizeHex(element.textColor))
            color = parsedCol
            alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
            textSize = (element.fontSize * scale).coerceAtLeast(9f)
            typeface = customTypeface
            isAntiAlias = true
            style = Paint.Style.FILL
            if (element.hasShadow) {
                setShadowLayer(4f, 2f, 2f, AndroidColor.parseColor(sanitizeHex(element.shadowColor)))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && element.letterSpacing != 0f) {
                letterSpacing = element.letterSpacing
            }
        }

        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, textPaint, availableWidth)
                .setAlignment(alignment)
                .setLineSpacing(2f, 1f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(text, textPaint, availableWidth, alignment, 1.0f, 2.0f, false)
        }

        val layoutH = staticLayout.height
        val startY = y + (h - layoutH) / 2f
        val startX = x + 4f

        canvas.nativeCanvas.save()
        canvas.nativeCanvas.translate(startX, startY)
        staticLayout.draw(canvas.nativeCanvas)
        canvas.nativeCanvas.restore()
    }
}

private fun DrawScope.drawBadgeElement(
    element: TemplateElement,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    canvasW: Float,
    context: Context? = null
) {
    val elOpacity = element.opacity.coerceIn(0f, 1f)
    val bgColor = element.backgroundColor?.let { parseColorHex(it).copy(alpha = elOpacity) } ?: Color(0xFFE11D48).copy(alpha = elOpacity)
    val corner = (h / 2f).coerceAtLeast(8f)

    drawRoundRect(
        color = bgColor,
        topLeft = Offset(x, y),
        size = Size(w, h),
        cornerRadius = CornerRadius(corner, corner)
    )

    element.text?.let { text ->
        drawContext.canvas.nativeCanvas.apply {
            val paint = Paint().apply {
                color = AndroidColor.parseColor(sanitizeHex(element.textColor))
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
                val scale = canvasW / 360f
                textSize = (element.fontSize * scale).coerceIn(9f, 24f)
                typeface = resolveTypeface(context, element.fontFamily, true)
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            val textBounds = Rect()
            paint.getTextBounds(text, 0, text.length, textBounds)
            val cx = x + w / 2f
            val cy = y + h / 2f + textBounds.height() / 2f - 1f
            drawText(text, cx, cy, paint)
        }
    }
}

private fun DrawScope.drawBrandLogoElement(
    element: TemplateElement,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    brandProfile: BrandProfile,
    context: Context? = null
) {
    val radius = minOf(w, h) / 2f
    val cx = x + w / 2f
    val cy = y + h / 2f
    val elOpacity = element.opacity.coerceIn(0f, 1f)

    // Check if user has imported a real logo image using LRU cache
    var logoBitmap: Bitmap? = null
    val logoKey = brandProfile.logoUri
    if (!logoKey.isNullOrBlank() && context != null) {
        logoBitmap = brandLogoLruCache.get(logoKey)
        if (logoBitmap == null) {
            try {
                val isFile = logoKey.startsWith("/") || logoKey.startsWith("file://")
                val decoded = if (isFile) {
                    val filePath = if (logoKey.startsWith("file://")) logoKey.removePrefix("file://") else logoKey
                    // Decode with boundary constraints to avoid OOM
                    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    BitmapFactory.decodeFile(filePath, options)
                    val targetDim = maxOf(w.toInt(), 512).coerceAtLeast(128)
                    var sample = 1
                    while (options.outWidth / (sample * 2) >= targetDim && options.outHeight / (sample * 2) >= targetDim) {
                        sample *= 2
                    }
                    val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sample }
                    BitmapFactory.decodeFile(filePath, decodeOpts)
                } else {
                    context.contentResolver.openInputStream(Uri.parse(logoKey))?.use {
                        BitmapFactory.decodeStream(it)
                    }
                }
                if (decoded != null) {
                    brandLogoLruCache.put(logoKey, decoded)
                    logoBitmap = decoded
                }
            } catch (e: Exception) {
                android.util.Log.e("TemplateRenderer", "Error decoding brand logo", e)
            }
        }
    }

    if (logoBitmap != null) {
        // Draw real logo clipped in circle with crisp border
        val clipPath = Path().apply {
            addRoundRect(RoundRect(left = x, top = y, right = x + w, bottom = y + h, cornerRadius = CornerRadius(radius, radius)))
        }
        clipPath(clipPath) {
            drawRect(color = Color.White.copy(alpha = elOpacity), topLeft = Offset(x, y), size = Size(w, h))
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
            }
            val srcRect = Rect(0, 0, logoBitmap.width, logoBitmap.height)
            val dstRect = RectF(x, y, x + w, y + h)
            drawContext.canvas.nativeCanvas.drawBitmap(logoBitmap, srcRect, dstRect, paint)
        }

        drawCircle(
            color = parseColorHex(brandProfile.primaryColorHex).copy(alpha = elOpacity),
            radius = radius,
            center = Offset(cx, cy),
            style = Stroke(width = 2.5f)
        )
    } else {
        // Fallback: draw circle with brand initial letter
        val brandInitial = if (brandProfile.brandName.isNotBlank()) {
            brandProfile.brandName.take(1)
        } else {
            "ب"
        }

        drawCircle(
            color = Color(0x33FFFFFF).copy(alpha = 0.2f * elOpacity),
            radius = radius,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = parseColorHex(brandProfile.primaryColorHex).copy(alpha = elOpacity),
            radius = radius,
            center = Offset(cx, cy),
            style = Stroke(width = 2.5f)
        )

        drawContext.canvas.nativeCanvas.apply {
            val paint = Paint().apply {
                color = AndroidColor.WHITE
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
                textSize = radius * 0.9f
                typeface = resolveTypeface(context, element.fontFamily, true)
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            drawText(brandInitial, cx, cy + radius * 0.35f, paint)
        }
    }
}

private fun formatPriceWithCommas(price: Long): String {
    val formatted = NumberFormat.getNumberInstance(Locale.US).format(price)
    return PersianDateUtil.toPersianDigits(formatted)
}

private fun DrawScope.drawPriceBlockElement(
    element: TemplateElement,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    canvasW: Float,
    context: Context? = null
) {
    val corner = (element.cornerRadius * (w / 300f)).coerceAtLeast(10f)
    val elOpacity = element.opacity.coerceIn(0f, 1f)
    val scale = canvasW / 360f

    // 1. Container background card
    val bgColor = element.backgroundColor?.let { parseColorHex(it).copy(alpha = elOpacity) }
        ?: Color(0xE618142A).copy(alpha = 0.9f * elOpacity)
    drawRoundRect(
        color = bgColor,
        topLeft = Offset(x, y),
        size = Size(w, h),
        cornerRadius = CornerRadius(corner, corner)
    )

    val strokeColor = element.borderColor?.let { parseColorHex(it).copy(alpha = elOpacity) }
        ?: Color(0x66A855F7).copy(alpha = 0.5f * elOpacity)
    val strokeWidth = if (element.borderWidth > 0f) element.borderWidth else 1.5f
    drawRoundRect(
        color = strokeColor,
        topLeft = Offset(x, y),
        size = Size(w, h),
        cornerRadius = CornerRadius(corner, corner),
        style = Stroke(width = strokeWidth)
    )

    // 2. Calculations
    val oldP = element.oldPrice ?: 0L
    val newP = element.newPrice ?: 0L
    val unit = element.currencyUnit.ifBlank { "تومان" }
    val discountPercent = element.discountPercentage

    val tf = resolveTypeface(context, element.fontFamily, true)

    drawContext.canvas.nativeCanvas.apply {
        val hasDiscount = discountPercent > 0 && oldP > newP

        // Top Row: Old price & Discount badge (if discount exists)
        if (hasDiscount) {
            val oldPriceText = "${formatPriceWithCommas(oldP)} $unit"
            val oldPaint = Paint().apply {
                color = AndroidColor.parseColor("#94A3B8")
                alpha = (200 * elOpacity).toInt().coerceIn(0, 255)
                textSize = (element.fontSize * scale * 0.75f).coerceIn(10f, 18f)
                typeface = tf
                textAlign = Paint.Align.RIGHT
                isAntiAlias = true
            }

            val badgeText = PersianDateUtil.toPersianDigits("٪$discountPercent تخفیف")
            val badgePaint = Paint().apply {
                color = AndroidColor.WHITE
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
                textSize = (element.fontSize * scale * 0.70f).coerceIn(9f, 16f)
                typeface = tf
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }

            val oldY = y + h * 0.38f
            val oldX = x + w - 16f * scale
            drawText(oldPriceText, oldX, oldY, oldPaint)

            // Strikethrough line over old price
            val textLen = oldPaint.measureText(oldPriceText)
            val strikePaint = Paint().apply {
                color = AndroidColor.parseColor("#EF4444")
                this.strokeWidth = 2.5f * scale
                isAntiAlias = true
            }
            val strikeY = oldY - (oldPaint.textSize * 0.35f)
            drawLine(oldX, strikeY, oldX - textLen, strikeY, strikePaint)

            // Draw Discount Pill Badge on the left
            val badgeW = (badgePaint.measureText(badgeText) + 18f * scale)
            val badgeH = h * 0.36f
            val badgeX = x + 14f * scale
            val badgeY = y + h * 0.14f
            val bRect = RectF(badgeX, badgeY, badgeX + badgeW, badgeY + badgeH)
            val bBgPaint = Paint().apply {
                color = AndroidColor.parseColor("#EF4444")
                alpha = (240 * elOpacity).toInt().coerceIn(0, 255)
                isAntiAlias = true
            }
            drawRoundRect(bRect, badgeH / 2f, badgeH / 2f, bBgPaint)
            val bTextY = badgeY + badgeH / 2f - (badgePaint.descent() + badgePaint.ascent()) / 2f
            drawText(badgeText, badgeX + badgeW / 2f, bTextY, badgePaint)

            // Bottom Row: New prominent price
            val newPriceFormatted = formatPriceWithCommas(newP)
            val newPricePaint = Paint().apply {
                color = AndroidColor.parseColor(sanitizeHex(element.textColor))
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
                textSize = (element.fontSize * scale * 1.25f).coerceIn(14f, 32f)
                typeface = tf
                textAlign = Paint.Align.RIGHT
                isAntiAlias = true
            }
            val unitPaint = Paint().apply {
                color = AndroidColor.parseColor("#FDE68A")
                alpha = (220 * elOpacity).toInt().coerceIn(0, 255)
                textSize = (element.fontSize * scale * 0.8f).coerceIn(10f, 18f)
                typeface = tf
                textAlign = Paint.Align.LEFT
                isAntiAlias = true
            }

            val newY = y + h * 0.82f
            val newX = x + w - 16f * scale
            drawText(newPriceFormatted, newX, newY, newPricePaint)

            val priceWidth = newPricePaint.measureText(newPriceFormatted)
            drawText(unit, newX - priceWidth - 8f * scale, newY, unitPaint)

        } else {
            // Single regular price row (centered or aligned)
            val displayPrice = if (newP > 0) newP else oldP
            val priceStr = "${formatPriceWithCommas(displayPrice)} $unit"
            val pricePaint = Paint().apply {
                color = AndroidColor.parseColor(sanitizeHex(element.textColor))
                alpha = (255 * elOpacity).toInt().coerceIn(0, 255)
                textSize = (element.fontSize * scale * 1.15f).coerceIn(13f, 30f)
                typeface = tf
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            val pY = y + h / 2f - (pricePaint.descent() + pricePaint.ascent()) / 2f
            drawText(priceStr, x + w / 2f, pY, pricePaint)
        }
    }
}

private fun parseColorHex(hex: String): Color {
    return try {
        Color(AndroidColor.parseColor(sanitizeHex(hex)))
    } catch (e: Exception) {
        Color(0xFF7C3AED)
    }
}

private fun sanitizeHex(hex: String): String {
    val clean = hex.trim()
    return if (clean.startsWith("#")) {
        if (clean.length == 7 || clean.length == 9) clean
        else if (clean.length == 4) {
            "#" + clean[1] + clean[1] + clean[2] + clean[2] + clean[3] + clean[3]
        } else "#7C3AED"
    } else {
        "#$clean"
    }
}
