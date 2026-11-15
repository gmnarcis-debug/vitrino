package red.line.instayar.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.FileProvider
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateFormat
import red.line.instayar.data.repository.UserPreferences
import red.line.instayar.security.AppIntegrityChecker
import red.line.instayar.security.IntegrityResult
import red.line.instayar.ui.components.drawTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.hypot
import kotlin.random.Random

/**
 * سبک‌های مختلف واترمارک برای کاربران رایگان و مقابله با دستکاری برنامه
 */
enum class WatermarkStyle {
    SUBTLE,     // فقط نوار پایین
    STRONG,     // الگوی مورب تکرارشونده در کل تصویر + نوار پایین
    AGGRESSIVE  // الگوی مورب + نوار پایین + برچسب دایره‌ای بزرگ در مرکز تصویر
}

object ImageExportUtil {

    // سبک پیش‌فرض واترمارک
    var defaultWatermarkStyle: WatermarkStyle = WatermarkStyle.STRONG

    /**
     * بررسی اعتبارسنجی رمزنگاری‌شده وضعیت VIP و سلامت برنامه
     * واترمارک فقط در صورت وجود خرید معتبر VIP با امضای دیجیتال RSA مایکت حذف می‌شود.
     * در صورت دستکاری برنامه یا عدم تطابق پکیج، سبک AGGRESSIVE اعمال می‌شود.
     */
    fun resolveWatermarkStyle(
        context: Context?,
        requestedStyle: WatermarkStyle? = null
    ): WatermarkStyle? {
        if (context == null) {
            // در غیاب کانتکست جهت اعتبارسنجی، واترمارک پیش‌فرض اجباری اعمال می‌شود
            return requestedStyle ?: defaultWatermarkStyle
        }

        // ۱. بررسی سلامت و یکپارچگی برنامه (ضد پچ، ضد دستکاری پکیج و فایل مانیفست)
        val integrity = AppIntegrityChecker.verifyAppIntegrity(context)
        if (integrity !is IntegrityResult.Valid) {
            // برنامه نامعتبر یا دستکاری شده است: اجبار بر سبک تهاجمی واترمارک
            return WatermarkStyle.AGGRESSIVE
        }

        // ۲. اعتبارسنجی رمزنگاری‌شده امضای RSA مایکت و تاریخ انقضا از UserPreferences
        val userPrefs = UserPreferences(context)
        val hasValidVip = userPrefs.verifyAndCalculateVipStatus()
        if (hasValidVip) {
            // کاربر دارای اشتراک معتبر VIP با امضای رسمی مایکت است؛ خروجی کاملاً بدون واترمارک
            return null
        }

        // کاربر رایگان است:
        // برای تضمین مقاومت در برابر برش ساده تصویر، سبک واترمارک همواره حداقل STRONG است
        return when (requestedStyle) {
            WatermarkStyle.AGGRESSIVE -> WatermarkStyle.AGGRESSIVE
            WatermarkStyle.STRONG -> WatermarkStyle.STRONG
            WatermarkStyle.SUBTLE -> WatermarkStyle.STRONG // ارتقا برای مقاومت کامل در برابر برش
            null -> defaultWatermarkStyle
        }
    }

    suspend fun renderTemplateToBitmap(
        template: Template,
        brandProfile: BrandProfile,
        context: Context? = null,
        style: WatermarkStyle? = null,
        isVip: Boolean? = null // حفظ پارامتر برای سازگاری امضا؛ وضعیت فقط با اعتبارسنجی امضای RSA تعیین می‌شود
    ): Bitmap = withContext(Dispatchers.Default) {
        val (targetW, targetH) = when (template.format) {
            TemplateFormat.STORY -> Pair(1080, 1920)
            TemplateFormat.POST_PORTRAIT -> Pair(1080, 1350)
            TemplateFormat.POST_SQUARE -> Pair(1080, 1080)
            TemplateFormat.HIGHLIGHT -> Pair(1080, 1080)
        }

        val bitmap = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val androidCanvas = android.graphics.Canvas(bitmap)
        val composeCanvas = Canvas(androidCanvas)

        val drawScope = CanvasDrawScope()
        drawScope.draw(
            density = Density(3f),
            layoutDirection = LayoutDirection.Rtl,
            canvas = composeCanvas,
            size = Size(targetW.toFloat(), targetH.toFloat())
        ) {
            drawTemplate(
                template = template,
                canvasSize = size,
                brandProfile = brandProfile,
                context = context
            )
        }

        // تعیین استایل واترمارک صرفاً بر اساس اعتبارسنجی رمزنگاری‌شده مایکت و سلامت برنامه
        // هیچ متغیر آزمایشی یا کلاینتی مجاز به دور زدن واترمارک نسخه رایگان نیست
        val watermarkToApply = resolveWatermarkStyle(context, style)

        // واترمارک مستقیماً و به صورت واقعی روی تصویر نهایی ادغام (Burn-in) می‌شود
        if (watermarkToApply != null) {
            drawWatermark(androidCanvas, targetW, targetH, watermarkToApply, bitmap = bitmap)
        }

        bitmap
    }

    /**
     * رسم واترمارک بر اساس استایل انتخابی همراه با فاز تصادفی جهت جلوگیری از حذف خودکار
     */
    fun drawWatermark(
        canvas: android.graphics.Canvas,
        w: Int,
        h: Int,
        style: WatermarkStyle = defaultWatermarkStyle,
        randomPhaseX: Float = (Random.nextFloat() * 80f) - 40f,
        randomPhaseY: Float = (Random.nextFloat() * 80f) - 40f,
        bitmap: Bitmap? = null
    ) {
        when (style) {
            WatermarkStyle.SUBTLE -> {
                drawBottomBar(canvas, w, h)
            }
            WatermarkStyle.STRONG -> {
                drawDiagonalPattern(canvas, w, h, randomPhaseX, randomPhaseY)
                drawBottomBar(canvas, w, h)
            }
            WatermarkStyle.AGGRESSIVE -> {
                drawDiagonalPattern(canvas, w, h, randomPhaseX, randomPhaseY)
                drawAggressiveCenterBadge(canvas, w, h)
                drawBottomBar(canvas, w, h)
            }
        }
    }

    /**
     * رسم الگوی مورب تکرارشونده روی کل تصویر با زاویه‌ی ۳۰- درجه
     * با رنگ سفید شفاف (~۳۰٪) و حاشیه/سایه تیره جهت خوانایی روی تمام پس‌زمینه‌ها
     */
    private fun drawDiagonalPattern(
        canvas: android.graphics.Canvas,
        w: Int,
        h: Int,
        phaseX: Float,
        phaseY: Float
    ) {
        val diagonalText = "ویترینو • نسخه رایگان       حذف با VIP"
        val fontSize = (w * 0.048f).coerceIn(36f, 60f)

        // قلم خط حاشیه تیره برای خوانایی روی سطوح روشن
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(130, 0, 0, 0)
            style = Paint.Style.STROKE
            strokeWidth = (w * 0.0035f).coerceIn(2.5f, 5.5f)
            textSize = fontSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        // قلم پرکننده سفید با شفافیت ۳۰٪ (حدود ۷۶ از ۲۵۵)
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(76, 255, 255, 255)
            style = Paint.Style.FILL
            textSize = fontSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        canvas.save()
        canvas.translate(w / 2f, h / 2f)
        canvas.rotate(-30f)

        val diag = hypot(w.toFloat(), h.toFloat())
        val stepY = (w * 0.25f).coerceAtLeast(180f)
        val stepX = (w * 0.70f).coerceAtLeast(420f)

        var y = -diag + phaseY
        var rowIndex = 0
        while (y <= diag + stepY) {
            val rowShift = ((rowIndex % 2) * (stepX * 0.5f)) + phaseX
            var x = -diag + rowShift
            while (x <= diag + stepX) {
                canvas.drawText(diagonalText, x, y, strokePaint)
                canvas.drawText(diagonalText, x, y, fillPaint)
                x += stepX
            }
            y += stepY
            rowIndex++
        }

        canvas.restore()
    }

    /**
     * نوار پایین نیمه‌شفاف تیره با نشان برند و پیام ترغیب به ارتقا
     */
    private fun drawBottomBar(canvas: android.graphics.Canvas, w: Int, h: Int) {
        val barHeight = (h * 0.056f).coerceIn(68f, 104f)
        val barTop = h - barHeight

        // پس‌زمینه نیمه‌شفاف تیره
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(225, 14, 11, 24)
            style = Paint.Style.FILL
        }
        val barRect = RectF(0f, barTop, w.toFloat(), h.toFloat())
        canvas.drawRect(barRect, bgPaint)

        // خط مرزی فوقانی با شیب/رنگ طلایی VIP
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(160, 245, 158, 11)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawLine(0f, barTop, w.toFloat(), barTop, borderPaint)

        // نشان مدور کوچک برند در سمت راست نوار
        val badgeRadius = barHeight * 0.26f
        val badgeX = w - (badgeRadius * 2.2f)
        val badgeY = barTop + (barHeight / 2f)

        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(230, 139, 92, 246)
            style = Paint.Style.FILL
        }
        canvas.drawCircle(badgeX, badgeY, badgeRadius, badgeBgPaint)

        val badgeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawCircle(badgeX, badgeY, badgeRadius, badgeBorderPaint)

        // حرف V داخل نشان
        val logoTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = badgeRadius * 1.3f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val logoTextY = badgeY - (logoTextPaint.descent() + logoTextPaint.ascent()) / 2f
        canvas.drawText("V", badgeX, logoTextY, logoTextPaint)

        // متن هشدار و تبلیغ VIP
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = (barHeight * 0.34f).coerceIn(24f, 34f)
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val text = "ساخته‌شده با ویترینو — برای حذف واترمارک VIP بگیرید"
        val textY = barTop + (barHeight / 2f) - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(text, w / 2f, textY, textPaint)
    }

    /**
     * برچسب گرد بزرگ و نیمه‌شفاف در مرکز تصویر برای حالت AGGRESSIVE
     */
    private fun drawAggressiveCenterBadge(canvas: android.graphics.Canvas, w: Int, h: Int) {
        val cx = w / 2f
        val cy = h / 2f
        val radius = (w * 0.28f).coerceIn(180f, 320f)

        // دایره پس‌زمینه نیمه‌شفاف تیره
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(190, 18, 14, 32)
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, radius, bgPaint)

        // حاشیه طلایی ضخیم
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(220, 245, 158, 11)
            style = Paint.Style.STROKE
            strokeWidth = 6f
        }
        canvas.drawCircle(cx, cy, radius, borderPaint)

        // حلقه نقطه‌چین داخلی
        val innerRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(120, 255, 255, 255)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawCircle(cx, cy, radius - 14f, innerRingPaint)

        // عنوان داخل برچسب: تاج و ویترینو
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(240, 245, 158, 11)
            textSize = (radius * 0.24f).coerceIn(34f, 62f)
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("👑 ویترینو", cx, cy - (radius * 0.35f), titlePaint)

        // زیرعنوان نسخه رایگان
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = (radius * 0.18f).coerceIn(24f, 44f)
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("نسخه رایگان آزمایشی", cx, cy - (radius * 0.05f), subPaint)

        // راهنمای خرید VIP
        val ctaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(240, 253, 230, 138)
            textSize = (radius * 0.13f).coerceIn(18f, 30f)
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("حذف دائم واترمارک با نسخه VIP", cx, cy + (radius * 0.28f), ctaPaint)

        val hintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(180, 203, 213, 225)
            textSize = (radius * 0.10f).coerceIn(15f, 24f)
            typeface = Typeface.DEFAULT
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("vitrino.app", cx, cy + (radius * 0.52f), hintPaint)
    }

    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        title: String,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): Uri? = withContext(Dispatchers.IO) {
        val isJpeg = compressFormat == Bitmap.CompressFormat.JPEG
        val ext = if (isJpeg) "jpg" else "png"
        val mimeType = if (isJpeg) "image/jpeg" else "image/png"
        val sanitizedTitle = title.trim().replace(Regex("[^a-zA-Z0-9_\\-\\u0600-\\u06FF]"), "_").take(40)
        val fileName = if (sanitizedTitle.isNotBlank()) {
            "${sanitizedTitle}_${System.currentTimeMillis()}.$ext"
        } else {
            "Vitrino_${System.currentTimeMillis()}.$ext"
        }
        val validQuality = quality.coerceIn(50, 100)
        var uri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.TITLE, if (title.isNotBlank()) title else fileName)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Vitrino")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return@withContext null

                try {
                    val outputStream = resolver.openOutputStream(uri)
                    if (outputStream == null) {
                        try { resolver.delete(uri, null, null) } catch (e: Exception) { e.printStackTrace() }
                        return@withContext null
                    }
                    val compressSucceeded = outputStream.use { stream ->
                        bitmap.compress(compressFormat, validQuality, stream)
                    }
                    if (!compressSucceeded) {
                        try { resolver.delete(uri, null, null) } catch (e: Exception) { e.printStackTrace() }
                        return@withContext null
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                } catch (writeError: Exception) {
                    try {
                        resolver.delete(uri, null, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return@withContext null
                }
            } else {
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(picturesDir, "Vitrino").apply { if (!exists()) mkdirs() }
                val file = File(appDir, fileName)
                val compressSucceeded = FileOutputStream(file).use { out ->
                    bitmap.compress(compressFormat, validQuality, out)
                }
                if (!compressSucceeded) {
                    if (file.exists()) file.delete()
                    return@withContext null
                }
                android.media.MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    arrayOf(mimeType),
                    null
                )
                uri = Uri.fromFile(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }

        uri
    }

    suspend fun shareBitmap(
        context: Context,
        bitmap: Bitmap,
        preferInstagram: Boolean = true
    ): Intent? = withContext(Dispatchers.IO) {
        try {
            val cacheImagesDir = File(context.cacheDir, "images").apply {
                if (!exists()) mkdirs()
            }
            // پاکسازی فایل‌های اشتراک‌گذاری قدیمی موقت بیش از ۱ ساعت گذشته
            try {
                val oneHourAgo = System.currentTimeMillis() - 3600_000L
                cacheImagesDir.listFiles()?.filter { it.lastModified() < oneHourAgo }?.forEach { it.delete() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val sharedFile = File(cacheImagesDir, "shared_story_${System.currentTimeMillis()}.png")
            val compressSuccess = FileOutputStream(sharedFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            if (!compressSuccess || !sharedFile.exists() || sharedFile.length() == 0L) {
                if (sharedFile.exists()) sharedFile.delete()
                return@withContext null
            }

            val fileUri: Uri = try {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    sharedFile
                )
            } catch (e: IllegalArgumentException) {
                // در محیط تست Robolectric یا تنظیمات خاص که مسیر روت ایزوله تغییر می‌کند
                Uri.fromFile(sharedFile)
            }

            if (preferInstagram) {
                val pm = context.packageManager
                val isInstagramInstalled = try {
                    pm.getPackageInfo("com.instagram.android", 0)
                    true
                } catch (e: Exception) {
                    false
                }

                if (isInstagramInstalled) {
                    // Try Instagram Stories intent first
                    val storyIntent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                        setDataAndType(fileUri, "image/png")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        putExtra("interactive_asset_uri", fileUri)
                    }

                    if (storyIntent.resolveActivity(pm) != null) {
                        return@withContext storyIntent
                    } else {
                        // Standard SEND intent directed to Instagram
                        val instagramSendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "image/png"
                            putExtra(Intent.EXTRA_STREAM, fileUri)
                            setPackage("com.instagram.android")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        if (instagramSendIntent.resolveActivity(pm) != null) {
                            return@withContext instagramSendIntent
                        }
                    }
                }
            }

            // Universal fallback chooser
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            Intent.createChooser(shareIntent, "اشتراک‌گذاری طرح با:")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
