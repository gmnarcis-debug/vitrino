package red.line.instayar.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.LruCache
import java.io.InputStream

/**
 * بارگذاری هوشمند و بهینه تصاویر قالب‌ها از assets یا گالری کاربر
 * با نمونه‌برداری کاهشی (inSampleSize) و کش درون‌حافظه‌ای جهت پیشگیری از پر شدن حافظه (OOM)
 */
object AssetImageLoader {

    // تخصیص حداکثر ۲۵ درصد از حافظه در دسترس به کش تصاویر (سقف ۳۲ مگابایت)
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = (maxMemory / 4).coerceIn(8 * 1024, 32 * 1024)

    private val memoryCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }

    /**
     * بارگذاری ایمن تصویر بر اساس نام فایل در assets یا آدرس محلی در حافظه دستگاه
     * در صورت نبود فایل یا بروز خطا، null برمی‌گرداند و به هیچ‌وجه کرش نمی‌کند.
     */
    fun loadBitmap(
        context: Context,
        assetName: String?,
        imageUri: String? = null,
        targetWidth: Int = 800,
        targetHeight: Int = 800
    ): Bitmap? {
        if (assetName.isNullOrBlank() && imageUri.isNullOrBlank()) {
            return null
        }

        val cacheKey = buildCacheKey(assetName, imageUri, targetWidth, targetHeight)
        synchronized(memoryCache) {
            val cached = memoryCache.get(cacheKey)
            if (cached != null && !cached.isRecycled) {
                return cached
            }
        }

        val bitmap = try {
            if (!imageUri.isNullOrBlank()) {
                loadFromUri(context, imageUri, targetWidth, targetHeight)
            } else if (!assetName.isNullOrBlank()) {
                loadFromAssets(context, assetName, targetWidth, targetHeight)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (bitmap != null) {
            synchronized(memoryCache) {
                memoryCache.put(cacheKey, bitmap)
            }
        }

        return bitmap
    }

    fun loadAssetBitmap(
        context: Context,
        assetName: String,
        targetWidth: Int = 800,
        targetHeight: Int = 800
    ): Bitmap? = loadBitmap(context, assetName, null, targetWidth, targetHeight)

    fun loadContentUriBitmap(
        context: Context,
        uriString: String,
        targetWidth: Int = 800,
        targetHeight: Int = 800
    ): Bitmap? = loadBitmap(context, null, uriString, targetWidth, targetHeight)

    private fun loadFromAssets(
        context: Context,
        assetName: String,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap? {
        val assetManager = context.assets
        val possiblePaths = listOf(
            if (assetName.startsWith("images/")) assetName else "images/$assetName",
            assetName,
            "assets/images/$assetName"
        )

        var selectedPath: String? = null
        for (path in possiblePaths) {
            try {
                assetManager.open(path).use {
                    selectedPath = path
                }
                break
            } catch (ignored: Exception) {}
        }

        val path = selectedPath ?: return null

        // ۱. اندازه‌گیری ابعاد واقعی تصویر بدون اختصاص حافظه
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        assetManager.open(path).use { input ->
            BitmapFactory.decodeStream(input, null, options)
        }

        if (options.outWidth <= 0 || options.outHeight <= 0) {
            return null
        }

        // ۲. محاسبه inSampleSize متناسب با ابعاد هدف
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        // ۳. بارگذاری تصویر بهینه‌شده
        return assetManager.open(path).use { input ->
            BitmapFactory.decodeStream(input, null, options)
        }
    }

    private fun loadFromUri(
        context: Context,
        uriString: String,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap? {
        val uri = Uri.parse(uriString)
        val resolver = context.contentResolver

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        resolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, options)
        } ?: return null

        if (options.outWidth <= 0 || options.outHeight <= 0) {
            return null
        }

        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        return resolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, options)
        }
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        return calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight)
    }

    fun calculateInSampleSize(
        actualWidth: Int,
        actualHeight: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = actualHeight
        val width = actualWidth
        var inSampleSize = 1

        val targetW = reqWidth.coerceAtLeast(100)
        val targetH = reqHeight.coerceAtLeast(100)

        if (height > targetH || width > targetW) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= targetH && (halfWidth / inSampleSize) >= targetW) {
                inSampleSize *= 2
            }
        }
        return inSampleSize.coerceAtLeast(1)
    }

    private fun buildCacheKey(
        assetName: String?,
        imageUri: String?,
        w: Int,
        h: Int
    ): String {
        val id = imageUri ?: assetName ?: ""
        // سایزبندی پله‌ای جهت افزایش نرخ اصابت کش
        val bucketW = ((w / 200) + 1) * 200
        val bucketH = ((h / 200) + 1) * 200
        return "${id}_${bucketW}x${bucketH}"
    }

    fun clearCache() {
        synchronized(memoryCache) {
            memoryCache.evictAll()
        }
    }
}
