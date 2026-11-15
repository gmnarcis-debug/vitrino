package red.line.instayar.billing

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Base64
import androidx.activity.result.ActivityResultRegistry
import com.android.vending.billing.IInAppBillingService
import com.farasource.billing.BillingClient
import com.farasource.billing.communication.OnBillingResultListener
import com.farasource.billing.util.Inventory
import com.farasource.billing.util.Purchase
import com.farasource.billing.util.TableCodes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import red.line.instayar.data.repository.UserPreferences
import red.line.instayar.security.AppIntegrityChecker
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

sealed class BillingResult {
    data class Success(val sku: String, val message: String) : BillingResult()
    data class Error(val errorMessage: String) : BillingResult()
    object Cancelled : BillingResult()
    data class MarketNotInstalled(val installUrl: String = "https://myket.ir") : BillingResult()
    data class Restored(val message: String) : BillingResult()
}

sealed class SkuPriceState {
    object Idle : SkuPriceState()
    object Loading : SkuPriceState()
    data class Success(val skus: List<SkuDetails>) : SkuPriceState()
    data class Error(val errorMessage: String, val isMarketMissing: Boolean = false) : SkuPriceState()
}

data class SkuDetails(
    val sku: String,
    val title: String,
    val period: String,
    val price: String = "",
    val discountPercent: String? = null,
    val isPopular: Boolean = false
)

class MyketBillingManager(
    private val context: Context,
    private val preferences: UserPreferences
) {
    companion object {
        const val SKU_VIP_MONTHLY = "vip_monthly"
        const val SKU_VIP_QUARTERLY = "vip_quarterly"
        const val SKU_VIP_LIFETIME = "vip_lifetime"

        const val MYKET_PACKAGE = "ir.mservices.market"
        const val MYKET_BILLING_ACTION = "ir.mservices.market.InAppBillingService.BIND"
        const val MYKET_INSTALL_URL = "https://myket.ir"
        const val EXPECTED_PACKAGE_NAME = "red.line.instayar"

        // کلید عمومی RSA مایکت به صورت تکه‌تکه و مبهم‌شده در کد
        private const val PK_SEGMENT_A = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpCSnaLO11Q1iqExPvGj8VK/"
        private const val PK_SEGMENT_B = "bqxt8O11vt9Q+Vj1P5M3RjUflbc0l6Y4QFwZOcN1K7zBEPpyc1CNqG7hASbDac"
        private const val PK_SEGMENT_C = "SZT8+zsZLL45IzRvdajSDJWljMI5lCI4nQU1GCFUoZ4RnVsKZx+uzZ+IFB1IvG"
        private const val PK_SEGMENT_D = "A3oJkZnDeWDDzC5aWEoDOkoQIDAQAB"

        // کلید و وضعیت قابل تنظیم برای تست‌های واحد Robolectric
        internal var testPublicKeyOverride: String? = null
        internal var isMyketInstalledOverride: Boolean? = null

        fun getMyketPublicKey(): String {
            return PK_SEGMENT_A + PK_SEGMENT_B + PK_SEGMENT_C + PK_SEGMENT_D
        }

        /**
         * متد تأیید امضای RSA با کلید عمومی رسمی مایکت (SHA1withRSA).
         * در نسخه ریلیز (!DEBUG)، برای امنیت کامل صرفاً از کلید رسمی مایکت استفاده می‌شود
         * و هرگونه کلید آزمایشی نادیده گرفته می‌شود.
         */
        fun verifyRsaSignature(signedData: String, signature: String): Boolean {
            if (signedData.isBlank() || signature.isBlank()) return false
            val publicKeyBase64 = if (red.line.instayar.BuildConfig.DEBUG) {
                testPublicKeyOverride ?: getMyketPublicKey()
            } else {
                getMyketPublicKey()
            }
            return try {
                val keyBytes = Base64.decode(publicKeyBase64, Base64.DEFAULT)
                val keyFactory = KeyFactory.getInstance("RSA")
                val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(keyBytes))

                val sig = Signature.getInstance("SHA1withRSA")
                sig.initVerify(publicKey)
                sig.update(signedData.toByteArray(Charsets.UTF_8))
                sig.verify(Base64.decode(signature, Base64.DEFAULT))
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        /**
         * اعتبارسنجی محتوای رسید خرید طبق ساختار رسمی In-App Billing مایکت:
         * ۱. بررسی نام پکیج (در صورت وجود، باید با شناسه رسمی اپلیکیشن تطابق داشته باشد).
         * ۲. بررسی شناسه معتبر محصول (productId یکی از سه پلن VIP).
         * ۳. بررسی زمان خرید (purchaseTime مثبت، غیرصفر و نه در آینده با تسامح ۵ دقیقه اختلاف ساعت).
         * ۴. بررسی وجود توکن خرید غیرخالی (purchaseToken).
         * ۵. بررسی وضعیت خرید (purchaseState برابر با ۰ در صورت وجود).
         */
        fun isPurchaseContentValid(json: JSONObject): Boolean {
            // ۱. بررسی نام پکیج رسمی برنامه (اجباری در تمام رسیدهای معتبر مایکت)
            val pkg = json.optString("packageName")
            if (pkg != EXPECTED_PACKAGE_NAME) {
                return false
            }

            // ۲. بررسی شناسه معتبر محصول
            val productId = json.optString("productId")
            val isValidSku = productId == SKU_VIP_MONTHLY || productId == SKU_VIP_QUARTERLY || productId == SKU_VIP_LIFETIME
            if (!isValidSku) return false

            // ۳. بررسی زمان تراکنش (purchaseTime)
            val purchaseTime = json.optLong("purchaseTime", -1L)
            if (purchaseTime <= 0L) return false
            val maxAllowedFutureTime = System.currentTimeMillis() + 300_000L
            if (purchaseTime > maxAllowedFutureTime) return false

            // ۴. بررسی توکن خرید
            val token = json.optString("purchaseToken", json.optString("token"))
            if (token.isBlank()) return false

            // ۵. بررسی وضعیت خرید (purchaseState: 0 یعنی خریداری‌شده معتبر)
            if (json.has("purchaseState")) {
                val state = json.optInt("purchaseState", -1)
                if (state != 0) return false
            }

            return true
        }

        /**
         * قالب‌بندی استاندارد قیمت دریافتی از مایکت:
         * تبدیل ارقام به فارسی، اعمال جداکننده ۳ رقمی هزارگان و اضافه کردن واحد پول (تومان).
         */
        fun formatMyketPrice(rawPrice: String, micros: Long = -1L): String {
            val clean = rawPrice.trim()
            if (clean.isNotBlank()) {
                val digitsOnly = clean.filter { it.isDigit() }
                if (digitsOnly.isNotBlank() && clean.none { it.isLetter() }) {
                    val amount = digitsOnly.toLongOrNull() ?: 0L
                    val formattedNum = java.text.DecimalFormat("#,###").format(amount)
                    return red.line.instayar.util.PersianDateUtil.toPersianDigits("$formattedNum تومان")
                }
                var formatted = red.line.instayar.util.PersianDateUtil.toPersianDigits(clean)
                if (!formatted.contains("تومان") && !formatted.contains("ریال")) {
                    formatted = "$formatted تومان"
                }
                return formatted
            }

            if (micros > 0L) {
                val amount = micros / 1_000_000L
                val formattedNum = java.text.DecimalFormat("#,###").format(amount)
                return red.line.instayar.util.PersianDateUtil.toPersianDigits("$formattedNum تومان")
            }

            return ""
        }
    }

    sealed class QueryPurchasesStatus {
        object ValidPurchaseFound : QueryPurchasesStatus()
        object NoPurchasesFound : QueryPurchasesStatus()
        object InvalidOrCorruptedPurchase : QueryPurchasesStatus()
        data class NetworkOrServiceError(val message: String) : QueryPurchasesStatus()
    }

    data class VerifyPurchasesResult(
        val hasActiveVip: Boolean,
        val activeSku: String? = null,
        val consumedAnyExpiredPurchase: Boolean = false,
        val status: QueryPurchasesStatus = QueryPurchasesStatus.NoPurchasesFound
    )

    fun getSkuTitle(sku: String?): String {
        return when (sku) {
            SKU_VIP_MONTHLY -> "اشتراک ۱ ماهه"
            SKU_VIP_QUARTERLY -> "اشتراک ۳ ماهه"
            SKU_VIP_LIFETIME -> "عضویت دائمی"
            else -> "نامشخص"
        }
    }

    fun verifyRsaSignature(signedData: String, signature: String): Boolean {
        return Companion.verifyRsaSignature(signedData, signature)
    }

    private val _availableSkus = MutableStateFlow(
        listOf(
            SkuDetails(
                sku = SKU_VIP_MONTHLY,
                title = "اشتراک ۱ ماهه",
                period = "۳۰ روز دسترسی کامل به همه امکانات",
                price = ""
            ),
            SkuDetails(
                sku = SKU_VIP_QUARTERLY,
                title = "اشتراک ۳ ماهه",
                period = "۹۰ روز دسترسی کامل (بهترین انتخاب)",
                price = "",
                discountPercent = "۳۰٪ تخفیف",
                isPopular = true
            ),
            SkuDetails(
                sku = SKU_VIP_LIFETIME,
                title = "عضویت دائمی",
                period = "دسترسی مادام‌العمر + آپدیت‌های آینده",
                price = "",
                discountPercent = "بهترین ارزش"
            )
        )
    )
    val availableSkus: StateFlow<List<SkuDetails>> = _availableSkus.asStateFlow()

    private val _skuPriceState = MutableStateFlow<SkuPriceState>(SkuPriceState.Loading)
    val skuPriceState: StateFlow<SkuPriceState> = _skuPriceState.asStateFlow()

    private val _isServiceConnected = MutableStateFlow(false)
    val isServiceConnected: StateFlow<Boolean> = _isServiceConnected.asStateFlow()

    private val _isCheckingPurchases = MutableStateFlow(false)
    val isCheckingPurchases: StateFlow<Boolean> = _isCheckingPurchases.asStateFlow()

    private var inAppBillingService: IInAppBillingService? = null
    private var farasourceClient: BillingClient? = null

    private var pendingPurchaseCallback: ((BillingResult) -> Unit)? = null
    private var lastRequestedSku: String? = null
    // فلگ مجاز بودن تلاش مجدد دقیقا یک‌بار برای خرید منقضی‌شده که مصرف شد
    private var canRetryExpiredPurchaseOnce = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            inAppBillingService = IInAppBillingService.Stub.asInterface(service)
            _isServiceConnected.value = true
            CoroutineScope(Dispatchers.IO).launch {
                verifyAllPurchasesFromMyket()
                querySkuPricesFromMyket()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            inAppBillingService = null
            _isServiceConnected.value = false
        }
    }

    init {
        bindMyketService()
    }

    fun isMyketInstalled(): Boolean {
        isMyketInstalledOverride?.let { return it }
        return try {
            val pm = context.packageManager
            pm.getPackageInfo(MYKET_PACKAGE, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    internal fun setPendingCallbackForTesting(callback: ((BillingResult) -> Unit)?) {
        pendingPurchaseCallback = callback
    }

    private fun bindMyketService() {
        if (!isMyketInstalled()) {
            _isServiceConnected.value = false
            _skuPriceState.value = SkuPriceState.Error(
                errorMessage = "برنامه مایکت روی دستگاه نصب نیست. برای استعلام قیمت و خرید، ابتدا مایکت را نصب کنید.",
                isMarketMissing = true
            )
            return
        }

        try {
            val intent = Intent(MYKET_BILLING_ACTION).apply {
                setPackage(MYKET_PACKAGE)
            }
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            e.printStackTrace()
            _isServiceConnected.value = false
        }
    }

    fun attachRegistry(registry: ActivityResultRegistry) {
        if (!isMyketInstalled()) return

        try {
            farasourceClient = BillingClient(registry, context).apply {
                setGlobalAutoConsume(false)
                setOnBillingResultListener(object : OnBillingResultListener {
                    override fun onBillingSuccess(purchase: Purchase?) {
                        handlePurchaseSuccess(purchase)
                    }

                    override fun onConsumeFinished(purchase: Purchase?, success: Boolean) {}

                    override fun onBillingStatus(code: Int) {
                        handleBillingStatus(code)
                    }

                    override fun onQueryInventoryFinished(inventory: Inventory?) {
                        handleInventoryFinished(inventory)
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkAndRestorePurchasesOnStartup(onResult: ((Boolean) -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            _isCheckingPurchases.value = true
            try {
                if (!isMyketInstalled()) {
                    val isVip = preferences.revalidateVipStatus()
                    withContext(Dispatchers.Main) {
                        onResult?.invoke(isVip)
                    }
                    _isCheckingPurchases.value = false
                    return@launch
                }

                val isValid = verifyAllPurchasesFromMyket()
                withContext(Dispatchers.Main) {
                    onResult?.invoke(isValid)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult?.invoke(preferences.revalidateVipStatus())
                }
            } finally {
                _isCheckingPurchases.value = false
            }
        }
    }

    /**
     * استعلام مستقیم خریدهای ثبت شده در مایکت، مصرف کردن خریدهای منقضی‌شده زمان‌دار،
     * و اعتبارسنجی امضای RSA کلید عمومی مایکت به همراه اعتبارسنجی محتوا (packageName و productId).
     */
    suspend fun verifyAllPurchasesFromMyketDetailed(): VerifyPurchasesResult = withContext(Dispatchers.IO) {
        val service = inAppBillingService
        if (service == null) {
            val isLocalVip = preferences.revalidateVipStatus()
            val activeSku = preferences.vipPlanType.value
            return@withContext VerifyPurchasesResult(
                hasActiveVip = isLocalVip,
                activeSku = activeSku,
                consumedAnyExpiredPurchase = false,
                status = if (isLocalVip && activeSku != null) QueryPurchasesStatus.ValidPurchaseFound else QueryPurchasesStatus.NetworkOrServiceError("سرویس پرداخت مایکت متصل نیست.")
            )
        }

        var consumedAnyExpired = false
        try {
            val ownedItemsBundle = service.getPurchases(3, context.packageName, "inapp", null)
            val responseCode = ownedItemsBundle.getInt("RESPONSE_CODE", -1)

            if (responseCode == 0) {
                val purchaseDataList = ownedItemsBundle.getStringArrayList("INAPP_PURCHASE_DATA_LIST") ?: arrayListOf()
                val signatureList = ownedItemsBundle.getStringArrayList("INAPP_DATA_SIGNATURE_LIST") ?: arrayListOf()

                var validActiveVipFound = false
                var activePurchaseJson: String? = null
                var activeSignature: String? = null
                var activeFoundSku: String? = null

                for (i in purchaseDataList.indices) {
                    val purchaseJson = purchaseDataList[i]
                    val signature = signatureList.getOrNull(i) ?: ""

                    // ۱. بررسی و اعتبارسنجی امضای RSA کلید عمومی مایکت
                    val isSignatureValid = verifyRsaSignature(purchaseJson, signature)
                    if (isSignatureValid) {
                        val json = try { JSONObject(purchaseJson) } catch (e: Exception) { null }
                        // ۲. بررسی صحت محتوا (packageName، مجاز بودن productId، زمان خرید و توکن)
                        if (json != null && isPurchaseContentValid(json)) {
                            val sku = json.optString("productId")
                            val token = json.optString("purchaseToken", json.optString("token"))
                            val purchaseTime = json.optLong("purchaseTime", -1L)

                            if (purchaseTime <= 0L || purchaseTime > System.currentTimeMillis() + 300_000L) {
                                continue
                            }

                            val isExpired = preferences.isPlanExpired(sku, purchaseTime)

                            if (isExpired) {
                                // تمدید پلن‌های زمان‌دار:
                                // با service.consumePurchase آن را در مایکت مصرف کن تا کاربر بتواند دوباره بخرد.
                                // پلن vip_lifetime هرگز مصرف نمی‌شود.
                                if (sku != SKU_VIP_LIFETIME && token.isNotBlank()) {
                                    try {
                                        val consumeResult = service.consumePurchase(3, context.packageName, token)
                                        if (consumeResult == 0) {
                                            consumedAnyExpired = true
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            } else {
                                // خرید معتبر و بدون انقضا پیدا شد
                                validActiveVipFound = true
                                activePurchaseJson = purchaseJson
                                activeSignature = signature
                                activeFoundSku = sku
                                // پلن مادام‌العمر بر همه اولویت دارد
                                if (sku == SKU_VIP_LIFETIME) break
                            }
                        }
                    }
                }

                if (validActiveVipFound && activePurchaseJson != null && activeSignature != null) {
                    preferences.recordValidPurchase(activePurchaseJson, activeSignature)
                    return@withContext VerifyPurchasesResult(
                        hasActiveVip = true,
                        activeSku = activeFoundSku,
                        consumedAnyExpiredPurchase = consumedAnyExpired,
                        status = QueryPurchasesStatus.ValidPurchaseFound
                    )
                } else {
                    preferences.clearVipStatus()
                    return@withContext VerifyPurchasesResult(
                        hasActiveVip = false,
                        activeSku = null,
                        consumedAnyExpiredPurchase = consumedAnyExpired,
                        status = QueryPurchasesStatus.NoPurchasesFound
                    )
                }
            } else {
                return@withContext VerifyPurchasesResult(
                    hasActiveVip = preferences.revalidateVipStatus(),
                    activeSku = preferences.vipPlanType.value,
                    consumedAnyExpiredPurchase = consumedAnyExpired,
                    status = QueryPurchasesStatus.NetworkOrServiceError("کد وضعیت استعلام مایکت: $responseCode")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext VerifyPurchasesResult(
                hasActiveVip = preferences.revalidateVipStatus(),
                activeSku = preferences.vipPlanType.value,
                consumedAnyExpiredPurchase = consumedAnyExpired,
                status = QueryPurchasesStatus.NetworkOrServiceError(e.message ?: "خطای ناشناخته در استعلام مایکت")
            )
        }
    }

    suspend fun verifyAllPurchasesFromMyket(): Boolean {
        return verifyAllPurchasesFromMyketDetailed().hasActiveVip
    }

    suspend fun querySkuPricesFromMyket() = withContext(Dispatchers.IO) {
        if (!isMyketInstalled()) {
            _skuPriceState.value = SkuPriceState.Error(
                errorMessage = "برنامه مایکت روی دستگاه نصب نیست. برای استعلام قیمت و خرید، ابتدا مایکت را نصب کنید.",
                isMarketMissing = true
            )
            return@withContext
        }

        val service = inAppBillingService
        if (service == null) {
            _skuPriceState.value = SkuPriceState.Error(
                errorMessage = "سرویس پرداخت مایکت در دسترس نیست. لطفاً اتصال اینترنت یا نصب مایکت را بررسی کنید.",
                isMarketMissing = false
            )
            return@withContext
        }

        try {
            _skuPriceState.value = SkuPriceState.Loading
            val queryBundle = Bundle().apply {
                putStringArrayList("ITEM_ID_LIST", arrayListOf(SKU_VIP_MONTHLY, SKU_VIP_QUARTERLY, SKU_VIP_LIFETIME))
            }
            val skuDetailsBundle = service.getSkuDetails(3, context.packageName, "inapp", queryBundle)
            val responseCode = skuDetailsBundle.getInt("RESPONSE_CODE", -1)

            if (responseCode == 0) {
                val detailsList = skuDetailsBundle.getStringArrayList("DETAILS_LIST")
                if (detailsList.isNullOrEmpty()) {
                    _skuPriceState.value = SkuPriceState.Error(
                        errorMessage = "اطلاعات قیمت محصولات از مایکت دریافت نشد."
                    )
                    return@withContext
                }

                val updatedList = _availableSkus.value.map { current ->
                    val matchedJsonStr = detailsList.firstOrNull {
                        try {
                            JSONObject(it).optString("productId") == current.sku
                        } catch (e: Exception) {
                            it.contains(current.sku)
                        }
                    }
                    if (matchedJsonStr != null) {
                        val obj = JSONObject(matchedJsonStr)
                        val rawPrice = obj.optString("price", "")
                        val micros = obj.optLong("price_amount_micros", -1L)
                        val formatted = formatMyketPrice(rawPrice, micros)
                        current.copy(price = formatted)
                    } else {
                        current
                    }
                }

                val allPricesValid = updatedList.all { it.price.isNotBlank() }
                if (!allPricesValid) {
                    _skuPriceState.value = SkuPriceState.Error(
                        errorMessage = "استعلام قیمت برخی اشتراک‌ها از مایکت با شکست مواجه شد."
                    )
                    return@withContext
                }

                _availableSkus.value = updatedList
                _skuPriceState.value = SkuPriceState.Success(updatedList)
            } else {
                _skuPriceState.value = SkuPriceState.Error(
                    errorMessage = "خطا در دریافت قیمت از مایکت (کد وضعیت مایکت: $responseCode)"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _skuPriceState.value = SkuPriceState.Error(
                errorMessage = "خطا در استعلام قیمت: ${e.localizedMessage ?: "نامشخص"}"
            )
        }
    }

    /**
     * درخواست استعلام مجدد قیمت‌ها با امکان اتصال مجدد سرویس
     */
    fun retryFetchPrices() {
        CoroutineScope(Dispatchers.IO).launch {
            _skuPriceState.value = SkuPriceState.Loading
            if (!isMyketInstalled()) {
                _skuPriceState.value = SkuPriceState.Error(
                    errorMessage = "برنامه مایکت روی دستگاه نصب نیست. برای استعلام قیمت و خرید، ابتدا مایکت را نصب کنید.",
                    isMarketMissing = true
                )
                return@launch
            }
            if (inAppBillingService == null) {
                bindMyketService()
            }
            querySkuPricesFromMyket()
        }
    }

    /**
     * شروع فرآیند خرید با SKU انتخاب‌شده
     */
    fun purchaseProduct(
        activity: Activity?,
        sku: String,
        onResult: (BillingResult) -> Unit
    ) {
        if (!AppIntegrityChecker.isIntegrityValid(context)) {
            onResult(BillingResult.Error("برنامه دستکاری شده است و دسترسی به پرداخت مایکت مسدود است."))
            return
        }

        if (!isMyketInstalled()) {
            onResult(BillingResult.MarketNotInstalled(MYKET_INSTALL_URL))
            return
        }

        pendingPurchaseCallback = onResult
        lastRequestedSku = sku
        canRetryExpiredPurchaseOnce = true // فلگ مجاز بودن دقیقاً یک‌بار شروع مجدد خرید

        CoroutineScope(Dispatchers.IO).launch {
            // قبل از شروع، وضعیت خریدهای قبلی را استعلام و خریدهای منقضی زمان‌دار را مصرف و آزاد کن
            val isAlreadyActive = verifyAllPurchasesFromMyket()
            if (isAlreadyActive && (preferences.vipPlanType.value == sku || preferences.vipPlanType.value == SKU_VIP_LIFETIME)) {
                withContext(Dispatchers.Main) {
                    onResult(BillingResult.Success(sku, "این محصول قبلاً خریداری شده و اکنون فعال است! ✨"))
                    pendingPurchaseCallback = null
                    canRetryExpiredPurchaseOnce = false
                }
                return@launch
            }

            withContext(Dispatchers.Main) {
                val client = farasourceClient
                if (client != null) {
                    try {
                        client.launchBilling(sku)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onResult(BillingResult.Error("خطا در باز کردن درگاه پرداخت مایکت: ${e.message}"))
                        pendingPurchaseCallback = null
                        canRetryExpiredPurchaseOnce = false
                    }
                } else {
                    onResult(BillingResult.Error("کلاینت پرداخت هنوز آماده نشده است. لطفاً چند لحظه بعد تلاش کنید."))
                    pendingPurchaseCallback = null
                    canRetryExpiredPurchaseOnce = false
                }
            }
        }
    }

    /**
     * مدیریت نتیجه خرید موفق از مایکت
     */
    fun handlePurchaseSuccess(purchase: Purchase?) {
        if (purchase == null) {
            pendingPurchaseCallback?.invoke(BillingResult.Error("اطلاعات خرید دریافت نشد."))
            pendingPurchaseCallback = null
            canRetryExpiredPurchaseOnce = false
            return
        }

        val json = purchase.originalJson
        val signature = purchase.signature

        // ۱. اعتبارسنجی امضای دیجیتال با کلید RSA رسمی مایکت
        val isValid = verifyRsaSignature(json, signature)
        if (!isValid) {
            preferences.clearVipStatus()
            pendingPurchaseCallback?.invoke(
                BillingResult.Error("امضای دیجیتال خرید نامعتبر است! تأیید تراکنش ناموفق بود.")
            )
            pendingPurchaseCallback = null
            canRetryExpiredPurchaseOnce = false
            return
        }

        // ۲. اعتبارسنجی محتوای خرید (packageName، مجاز بودن productId، زمان خرید و توکن)
        val purchaseJsonObject = try { JSONObject(json) } catch (e: Exception) { null }
        if (purchaseJsonObject == null || !isPurchaseContentValid(purchaseJsonObject)) {
            preferences.clearVipStatus()
            pendingPurchaseCallback?.invoke(
                BillingResult.Error("محتوای خرید نامعتبر است (شناسه بسته، زمان یا محصول مطابقت ندارد).")
            )
            pendingPurchaseCallback = null
            canRetryExpiredPurchaseOnce = false
            return
        }

        val sku = purchase.sku
        val jsonProductId = purchaseJsonObject.optString("productId")
        if (sku != jsonProductId) {
            preferences.clearVipStatus()
            pendingPurchaseCallback?.invoke(
                BillingResult.Error("شناسه محصول خریداری‌شده ($sku) با شناسه محصول رسید ($jsonProductId) تطابق ندارد.")
            )
            pendingPurchaseCallback = null
            canRetryExpiredPurchaseOnce = false
            return
        }

        if (lastRequestedSku != null && lastRequestedSku != sku) {
            preferences.clearVipStatus()
            pendingPurchaseCallback?.invoke(
                BillingResult.Error("محصول خریداری‌شده ($sku) با محصول درخواستی ($lastRequestedSku) تطابق ندارد.")
            )
            pendingPurchaseCallback = null
            canRetryExpiredPurchaseOnce = false
            return
        }

        val saved = preferences.recordValidPurchase(json, signature)
        if (saved) {
            pendingPurchaseCallback?.invoke(
                BillingResult.Success(sku, "خرید با موفقیت در مایکت تأیید و عضویت طلایی فعال شد! 👑")
            )
        } else {
            pendingPurchaseCallback?.invoke(
                BillingResult.Error("خطا در اعتبارسنجی یا ذخیره اطلاعات خرید.")
            )
        }
        pendingPurchaseCallback = null
        canRetryExpiredPurchaseOnce = false
    }

    /**
     * مدیریت کدهای وضعیت مایکت و farasource:
     * - کد BILLING_FAILED و هر کد خطا/لغو نباید خرید را دوباره شروع کند.
     * - فقط در حالت ITEM_ALREADY_OWNED (کد ۷) و فقط در صورت مصرف خرید منقضی‌شده، دقیقاً یک‌بار مجدداً آغاز می‌شود.
     * - لغو کاربر با پیام «خرید لغو شد» خاتمه می‌یابد بدون پنجره جدید.
     */
    fun handleBillingStatus(code: Int) {
        // کد ۷ در پروتکل پرداخت مایکت: ITEM_ALREADY_OWNED
        if (code == 7) {
            handleItemAlreadyOwned()
            return
        }

        // کد ۱ (USER_CANCELED) یا کد -1005 (IABHELPER_USER_CANCELLED)
        if (code == 1 || code == -1005) {
            canRetryExpiredPurchaseOnce = false
            pendingPurchaseCallback?.invoke(BillingResult.Cancelled)
            pendingPurchaseCallback = null
            return
        }

        when (code) {
            TableCodes.MARKET_NOT_INSTALLED -> {
                canRetryExpiredPurchaseOnce = false
                pendingPurchaseCallback?.invoke(BillingResult.MarketNotInstalled(MYKET_INSTALL_URL))
                pendingPurchaseCallback = null
            }
            TableCodes.NO_NETWORK -> {
                canRetryExpiredPurchaseOnce = false
                pendingPurchaseCallback?.invoke(BillingResult.Error("اتصال اینترنت برقرار نیست. لطفاً شبکه را بررسی کنید."))
                pendingPurchaseCallback = null
            }
            TableCodes.BILLING_FAILED -> {
                // جلوگیری کامل از حلقه: به هیچ وجه خرید را تکرار نکن
                canRetryExpiredPurchaseOnce = false
                pendingPurchaseCallback?.invoke(BillingResult.Error("خرید لغو شد یا انجام نشد."))
                pendingPurchaseCallback = null
            }
            TableCodes.SETUP_FAILED -> {
                canRetryExpiredPurchaseOnce = false
                pendingPurchaseCallback?.invoke(BillingResult.Error("ارتباط اولیه با مایکت برقرار نشد."))
                pendingPurchaseCallback = null
            }
            else -> {
                if (code != TableCodes.SETUP_SUCCESS) {
                    canRetryExpiredPurchaseOnce = false
                    pendingPurchaseCallback?.invoke(BillingResult.Error("خرید لغو شد."))
                    pendingPurchaseCallback = null
                }
            }
        }
    }

    /**
     * مدیریت وضعیت ITEM_ALREADY_OWNED (کد ۷):
     * تطابق دقیق محصول درخواست‌شده با محصول مالکیت‌شده در مایکت.
     * مالکیت اشتراک ۱ ماهه نباید به عنوان خرید موفق اشتراک ۳ ماهه یا دائمی تلقی شود.
     */
    private fun handleItemAlreadyOwned() {
        val requestedSku = lastRequestedSku ?: preferences.vipPlanType.value ?: SKU_VIP_MONTHLY
        CoroutineScope(Dispatchers.IO).launch {
            val verificationResult = verifyAllPurchasesFromMyketDetailed()
            withContext(Dispatchers.Main) {
                when (verificationResult.status) {
                    is QueryPurchasesStatus.NetworkOrServiceError -> {
                        pendingPurchaseCallback?.invoke(
                            BillingResult.Error("خطا در بررسی وضعیت خرید مایکت. لطفاً اتصال اینترنت را بررسی کنید.")
                        )
                        pendingPurchaseCallback = null
                        canRetryExpiredPurchaseOnce = false
                    }
                    is QueryPurchasesStatus.ValidPurchaseFound -> {
                        if (verificationResult.activeSku == requestedSku) {
                            pendingPurchaseCallback?.invoke(
                                BillingResult.Success(requestedSku, "این محصول قبلاً خریداری شده و اکنون فعال است! ✨")
                            )
                        } else {
                            val activeTitle = getSkuTitle(verificationResult.activeSku)
                            val requestedTitle = getSkuTitle(requestedSku)
                            pendingPurchaseCallback?.invoke(
                                BillingResult.Error("شما در حال حاضر دارای $activeTitle فعال هستید و این با محصول درخواستی ($requestedTitle) تطابق ندارد.")
                            )
                        }
                        pendingPurchaseCallback = null
                        canRetryExpiredPurchaseOnce = false
                    }
                    is QueryPurchasesStatus.NoPurchasesFound -> {
                        if (verificationResult.consumedAnyExpiredPurchase && canRetryExpiredPurchaseOnce) {
                            canRetryExpiredPurchaseOnce = false
                            try {
                                val client = farasourceClient
                                if (client != null) {
                                    client.launchBilling(requestedSku)
                                } else {
                                    pendingPurchaseCallback?.invoke(
                                        BillingResult.Error("اشتراک قبلی شما منقضی و آزاد شد. لطفاً دوباره دکمه خرید را بزنید.")
                                    )
                                    pendingPurchaseCallback = null
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                pendingPurchaseCallback?.invoke(
                                    BillingResult.Error("اشتراک قبلی شما منقضی و آزاد شد. لطفاً دوباره دکمه خرید را بزنید.")
                                )
                                pendingPurchaseCallback = null
                            }
                        } else {
                            canRetryExpiredPurchaseOnce = false
                            pendingPurchaseCallback?.invoke(
                                BillingResult.Error("این محصول قبلاً خریداری شده است اما در حال حاضر فعال نیست. در صورت نیاز از دکمه «بازیابی خرید» استفاده کنید.")
                            )
                            pendingPurchaseCallback = null
                        }
                    }
                    is QueryPurchasesStatus.InvalidOrCorruptedPurchase -> {
                        canRetryExpiredPurchaseOnce = false
                        pendingPurchaseCallback?.invoke(
                            BillingResult.Error("اطلاعات خرید دریافت شده از مایکت نامعتبر است.")
                        )
                        pendingPurchaseCallback = null
                    }
                }
            }
        }
    }

    private fun handleInventoryFinished(inventory: Inventory?) {
        if (inventory == null) return
        val purchases = inventory.allPurchases
        for (purchase in purchases) {
            if (verifyRsaSignature(purchase.originalJson, purchase.signature)) {
                val json = try { JSONObject(purchase.originalJson) } catch (e: Exception) { null }
                if (json != null && isPurchaseContentValid(json)) {
                    val jsonProductId = json.optString("productId")
                    if (purchase.sku != jsonProductId) {
                        continue
                    }
                    val purchaseTime = json.optLong("purchaseTime", -1L)
                    val isExpired = preferences.isPlanExpired(purchase.sku, purchaseTime)
                    if (isExpired) {
                        if (purchase.sku != SKU_VIP_LIFETIME && purchase.token.isNotBlank()) {
                            farasourceClient?.consume(purchase)
                        }
                    } else {
                        preferences.recordValidPurchase(purchase.originalJson, purchase.signature)
                        return
                    }
                }
            }
        }
    }

    fun restorePurchases(onResult: (Boolean, String) -> Unit) {
        if (!isMyketInstalled()) {
            onResult(false, "برای بازیابی خرید، لطفاً اپلیکیشن مایکت را نصب داشته باشید.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val isRestored = verifyAllPurchasesFromMyket()
            withContext(Dispatchers.Main) {
                if (isRestored) {
                    onResult(true, "اشتراک طلایی شما با موفقیت از مایکت بازیابی شد! 🎉")
                } else {
                    onResult(false, "هیچ خرید فعال و معتبری در حساب مایکت شما یافت نشد.")
                }
            }
        }
    }

    fun destroy() {
        try {
            if (_isServiceConnected.value) {
                context.unbindService(serviceConnection)
            }
            farasourceClient?.endConnection()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
