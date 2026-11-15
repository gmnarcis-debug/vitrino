package red.line.instayar.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object StoragePermissionHelper {

    /** مجوز لازم. روی API 29+ هیچ مجوزی لازم نیست. */
    fun requiredPermission(): String? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) null
        else Manifest.permission.WRITE_EXTERNAL_STORAGE

    /** آیا این دستگاه نیاز به درخواست runtime دارد؟ */
    fun requiresRuntimePermission(): Boolean = requiredPermission() != null

    /** آیا مجوز داده شده (یا اصلاً لازم نیست)؟ */
    fun hasPermission(context: Context): Boolean {
        val perm = requiredPermission() ?: return true
        return ContextCompat.checkSelfPermission(
            context, perm
        ) == PackageManager.PERMISSION_GRANTED
    }
}