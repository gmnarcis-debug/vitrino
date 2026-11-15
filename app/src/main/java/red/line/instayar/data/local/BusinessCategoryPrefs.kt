package red.line.instayar.data.local

import android.content.Context

/**
 * مدیریت ذخیره‌سازی دسته کسب‌وکار انتخابی کاربر در یک SharedPreferences کاملاً جدا و جدید
 * (مستقل از UserPreferences به منظور حفظ امنیت و تفکیک داده‌ها)
 */
object BusinessCategoryPrefs {
    private const val PREFS_NAME = "vitrino_business_category_prefs"
    private const val KEY_SELECTED_CATEGORY = "selected_business_category"

    const val CAT_CLOTHING = "پوشاک"
    const val CAT_CAFE = "شیرینی و کافه"
    const val CAT_BEAUTY = "آرایشی و زیبایی"
    const val CAT_DIGITAL = "دیجیتال"
    const val CAT_FLOWERS = "گل و هدیه"
    const val CAT_OTHER = "سایر"

    val ALL_CATEGORIES = listOf(
        CAT_CLOTHING,
        CAT_CAFE,
        CAT_BEAUTY,
        CAT_DIGITAL,
        CAT_FLOWERS,
        CAT_OTHER
    )

    fun getSelectedCategory(context: Context): String {
        val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sp.getString(KEY_SELECTED_CATEGORY, CAT_CLOTHING) ?: CAT_CLOTHING
    }

    fun setSelectedCategory(context: Context, category: String) {
        val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sp.edit().putString(KEY_SELECTED_CATEGORY, category).apply()
    }
}
