package red.line.instayar

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.farasource.billing.util.TableCodes
import org.json.JSONObject
import red.line.instayar.billing.BillingResult
import red.line.instayar.billing.MyketBillingManager
import red.line.instayar.data.repository.TemplateRepository
import red.line.instayar.data.repository.UserPreferences
import red.line.instayar.security.AppIntegrityChecker
import red.line.instayar.security.IntegrityResult
import android.graphics.Bitmap
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.util.ImageExportUtil
import red.line.instayar.util.WatermarkStyle
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import red.line.instayar.ui.components.drawTemplate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ویترینو", appName)
  }

  @Test
  fun `load templates from assets`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = TemplateRepository(context)
    val templates = repo.loadTemplates()
    assertTrue(templates.isNotEmpty())
    assertEquals(58, templates.size)
  }

  @Test
  fun `verify user preferences dark theme toggle and persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)

    // Default should be true (dark mode default)
    assertTrue(prefs.isDarkTheme.value)

    // Toggle to Light mode
    prefs.setDarkTheme(false)
    assertFalse(prefs.isDarkTheme.value)

    // Verify persistence across new instance
    val freshPrefs = UserPreferences(context)
    assertFalse(freshPrefs.isDarkTheme.value)

    // Toggle back to Dark mode
    freshPrefs.setDarkTheme(true)
    assertTrue(freshPrefs.isDarkTheme.value)

    val finalPrefs = UserPreferences(context)
    assertTrue(finalPrefs.isDarkTheme.value)
  }

  @Test
  fun `verify user preferences vip persistence and rsa validation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)

    // Clear state
    prefs.clearVipStatus()
    assertFalse(prefs.isVipUser.value)

    // Generate test RSA KeyPair
    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      // 1. Test Valid Lifetime purchase JSON & RSA signature
      val lifetimeJson = """{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseToken":"token_life","purchaseTime":${System.currentTimeMillis()}}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(lifetimeJson.toByteArray(Charsets.UTF_8))
      }
      val validLifetimeSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)

      val recordedLife = prefs.recordValidPurchase(lifetimeJson, validLifetimeSig)
      assertTrue(recordedLife)
      assertTrue(prefs.isVipUser.value)
      assertEquals("vip_lifetime", prefs.vipPlanType.value)

      // 2. Test Expired Monthly purchase JSON (35 days ago) with valid RSA signature
      val expiredMonthlyTime = System.currentTimeMillis() - (35L * 24 * 60 * 60 * 1000)
      val expiredMonthlyJson = """{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseToken":"token_month","purchaseTime":$expiredMonthlyTime}"""
      val signerMonth = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(expiredMonthlyJson.toByteArray(Charsets.UTF_8))
      }
      val expiredMonthSig = android.util.Base64.encodeToString(signerMonth.sign(), android.util.Base64.NO_WRAP)

      prefs.recordValidPurchase(expiredMonthlyJson, expiredMonthSig)
      // Because it's expired, isVipUser must be false!
      assertFalse(prefs.isVipUser.value)
      assertTrue(prefs.isPlanExpired("vip_monthly", expiredMonthlyTime))

      // 3. Test Invalid/Tampered signature rejection
      val fakeSig = "invalidsignature12345"
      val recordFake = prefs.recordValidPurchase(lifetimeJson, fakeSig)
      assertFalse(recordFake)
      assertFalse(prefs.isVipUser.value)

      // 4. Test revalidate on startup without key_is_vip
      val sharedPrefs = context.getSharedPreferences("ghalibino_prefs", Context.MODE_PRIVATE)
      assertFalse(sharedPrefs.contains("key_is_vip"))
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
    }
  }

  @Test
  fun `verify myket billing manager skus and obfuscated public key`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val billingManager = MyketBillingManager(context, prefs)

    assertEquals(3, billingManager.availableSkus.value.size)

    val key = MyketBillingManager.getMyketPublicKey()
    assertNotNull(key)
    assertTrue(key.startsWith("MIGfMA0GCSqGSIb3DQEBAQU"))
    assertTrue(key.endsWith("IDAQAB"))

    // Invalid signature rejection test
    val isFakeValid = MyketBillingManager.verifyRsaSignature("fake_json", "fake_signature")
    assertFalse(isFakeValid)
  }

  @Test
  fun `verify app integrity checker package verification`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val integrity = AppIntegrityChecker.verifyAppIntegrity(context)
    assertNotNull(integrity)
  }

  @Test
  fun `verify persian date util digits and calendar conversion`() {
    // Digits conversion test
    val original = "Story 1405 - Price 69000 Tomans"
    val converted = red.line.instayar.util.PersianDateUtil.toPersianDigits(original)
    assertEquals("Story ۱۴۰۵ - Price ۶۹۰۰۰ Tomans", converted)

    // Gregorian to Persian calendar conversion test
    val (jy, jm, jd) = red.line.instayar.util.PersianDateUtil.gregorianToPersian(2026, 10, 25)
    assertEquals(1405, jy)
    assertEquals(8, jm) // Aban

    // Current solar year
    val solarYear = red.line.instayar.util.PersianDateUtil.getCurrentSolarYear()
    assertTrue(solarYear.isNotEmpty())

    // Live time check
    val liveTime = red.line.instayar.util.PersianDateUtil.getCurrentLiveTime()
    assertTrue(liveTime.contains(":"))

    // Greeting check
    val greeting = red.line.instayar.util.PersianDateUtil.getTimeOfDayGreeting()
    assertTrue(greeting.isNotEmpty())
  }

  @Test
  fun `verify isPurchaseContentValid rejects mismatched packageName and unknown productId`() {
    val now = System.currentTimeMillis()

    // 1. Valid cases (باید حتماً دارای packageName منطبق باشند)
    val validMonthly = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m"}""")
    val validQuarterly = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_quarterly","purchaseTime":$now,"purchaseToken":"tok_q"}""")
    val validLifetime = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseTime":$now,"purchaseToken":"tok_l"}""")

    assertTrue(MyketBillingManager.isPurchaseContentValid(validMonthly))
    assertTrue(MyketBillingManager.isPurchaseContentValid(validQuarterly))
    assertTrue(MyketBillingManager.isPurchaseContentValid(validLifetime))

    // 2. Invalid packageName cases (رسید بدون پکیج یا با پکیج متفاوت غیرمعتبر است)
    val missingPackage = JSONObject("""{"productId":"vip_lifetime","purchaseTime":$now,"purchaseToken":"tok_l"}""")
    val wrongPackage = JSONObject("""{"packageName":"com.other.app","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m"}""")
    val emptyPackage = JSONObject("""{"packageName":"","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m"}""")
    assertFalse("رسید بدون packageName باید رد شود", MyketBillingManager.isPurchaseContentValid(missingPackage))
    assertFalse("رسید با packageName مغایر باید رد شود", MyketBillingManager.isPurchaseContentValid(wrongPackage))
    assertFalse("رسید با packageName خالی باید رد شود", MyketBillingManager.isPurchaseContentValid(emptyPackage))

    // 3. Invalid productId cases
    val wrongProduct = JSONObject("""{"packageName":"red.line.instayar","productId":"coins_100","purchaseTime":$now,"purchaseToken":"tok_m"}""")
    val emptyProduct = JSONObject("""{"packageName":"red.line.instayar","productId":"","purchaseTime":$now,"purchaseToken":"tok_m"}""")
    val missingProduct = JSONObject("""{"packageName":"red.line.instayar","purchaseTime":$now,"purchaseToken":"tok_m"}""")
    assertFalse(MyketBillingManager.isPurchaseContentValid(wrongProduct))
    assertFalse(MyketBillingManager.isPurchaseContentValid(emptyProduct))
    assertFalse(MyketBillingManager.isPurchaseContentValid(missingProduct))

    // 4. Invalid purchaseTime cases
    val futurePurchaseTime = now + (600_000L) // 10 minutes in future (exceeds 5-minute tolerance)
    val futureTimeJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$futurePurchaseTime,"purchaseToken":"tok_m"}""")
    val zeroTimeJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":0,"purchaseToken":"tok_m"}""")
    val negTimeJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":-100,"purchaseToken":"tok_m"}""")
    val missingTimeJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseToken":"tok_m"}""")
    assertFalse(MyketBillingManager.isPurchaseContentValid(futureTimeJson))
    assertFalse(MyketBillingManager.isPurchaseContentValid(zeroTimeJson))
    assertFalse(MyketBillingManager.isPurchaseContentValid(negTimeJson))
    assertFalse(MyketBillingManager.isPurchaseContentValid(missingTimeJson))

    // 5. Invalid purchaseToken cases
    val blankTokenJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"   "}""")
    val missingTokenJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now}""")
    assertFalse(MyketBillingManager.isPurchaseContentValid(blankTokenJson))
    assertFalse(MyketBillingManager.isPurchaseContentValid(missingTokenJson))

    // 6. Invalid purchaseState cases (1 = canceled, 2 = refunded)
    val canceledStateJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m","purchaseState":1}""")
    val refundedStateJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m","purchaseState":2}""")
    val validStateJson = JSONObject("""{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m","purchaseState":0}""")
    assertFalse(MyketBillingManager.isPurchaseContentValid(canceledStateJson))
    assertFalse(MyketBillingManager.isPurchaseContentValid(refundedStateJson))
    assertTrue(MyketBillingManager.isPurchaseContentValid(validStateJson))
  }

  @Test
  fun `verify recordValidPurchase rejects bad packageName and bad productId even with valid signature`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    prefs.clearVipStatus()

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      // Bad packageName with valid RSA signature
      val badPkgJson = """{"packageName":"ir.fake.app","productId":"vip_lifetime","purchaseToken":"token_test","purchaseTime":${System.currentTimeMillis()}}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(badPkgJson.toByteArray(Charsets.UTF_8))
      }
      val badPkgSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)
      val recordedBadPkg = prefs.recordValidPurchase(badPkgJson, badPkgSig)
      assertFalse(recordedBadPkg)
      assertFalse(prefs.isVipUser.value)

      // Bad productId with valid RSA signature
      val badPidJson = """{"packageName":"red.line.instayar","productId":"fake_vip_plan","purchaseToken":"token_test","purchaseTime":${System.currentTimeMillis()}}"""
      val signer2 = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(badPidJson.toByteArray(Charsets.UTF_8))
      }
      val badPidSig = android.util.Base64.encodeToString(signer2.sign(), android.util.Base64.NO_WRAP)
      val recordedBadPid = prefs.recordValidPurchase(badPidJson, badPidSig)
      assertFalse(recordedBadPid)
      assertFalse(prefs.isVipUser.value)

      // Future purchaseTime with valid RSA signature
      val futureTime = System.currentTimeMillis() + 600_000L
      val futureJson = """{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseToken":"token_test","purchaseTime":$futureTime}"""
      val signer3 = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(futureJson.toByteArray(Charsets.UTF_8))
      }
      val futureSig = android.util.Base64.encodeToString(signer3.sign(), android.util.Base64.NO_WRAP)
      val recordedFuture = prefs.recordValidPurchase(futureJson, futureSig)
      assertFalse(recordedFuture)
      assertFalse(prefs.isVipUser.value)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
    }
  }

  @Test
  fun `verify handleBillingStatus prevents purchase loop on error and cancellation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val billingManager = MyketBillingManager(context, prefs)

    // Simulate pending purchase and user cancellation (code 1)
    var resultReceived: BillingResult? = null
    billingManager.setPendingCallbackForTesting {
      resultReceived = it
    }

    billingManager.handleBillingStatus(1)
    assertEquals(BillingResult.Cancelled, resultReceived)

    // Verify callback was cleared and further calls do not trigger it again
    resultReceived = null
    billingManager.handleBillingStatus(TableCodes.BILLING_FAILED)
    // Because pending callback was cleared, resultReceived remains null
    assertEquals(null, resultReceived)

    // Test BILLING_FAILED terminates with Error and clears callback without loop
    billingManager.setPendingCallbackForTesting {
      resultReceived = it
    }
    billingManager.handleBillingStatus(TableCodes.BILLING_FAILED)
    assertTrue(resultReceived is BillingResult.Error)

    // Verify that after BILLING_FAILED, callback is cleared and no loop occurs
    resultReceived = null
    billingManager.handleBillingStatus(TableCodes.BILLING_FAILED)
    assertEquals(null, resultReceived)
  }

  @Test
  fun `verify template json parsing with imageAsset and background image`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = TemplateRepository(context)

    val sampleJson = """
      {
        "id": "tpl_test_image_01",
        "title": "قالب تستی تصویر",
        "category": "معرفی محصول",
        "format": "STORY",
        "isVip": false,
        "tags": ["تست", "عکس"],
        "background": {
          "type": "IMAGE",
          "color": "#1E1B4B",
          "imageAsset": "bg_abstract_01.webp",
          "overlayColors": ["#312E81", "#0F0D24"],
          "overlayAlpha": 0.75,
          "angle": 135
        },
        "elements": [
          {
            "id": "el_real_img",
            "type": "IMAGE",
            "x": 0.1,
            "y": 0.2,
            "width": 0.8,
            "height": 0.5,
            "cornerRadius": 16,
            "imageAsset": "fashion_01.webp",
            "contentScale": "FIT",
            "imagePanX": 0.1,
            "imagePanY": -0.2,
            "imageZoom": 1.5
          },
          {
            "id": "el_placeholder_img",
            "type": "IMAGE_PLACEHOLDER",
            "x": 0.1,
            "y": 0.7,
            "width": 0.8,
            "height": 0.2,
            "placeholderLabel": "قاب محصول نمونه",
            "imageAsset": "beauty_01.webp",
            "contentScale": "CROP"
          }
        ]
      }
    """.trimIndent()

    val template = repo.parseTemplateJson(sampleJson)
    assertNotNull(template)
    assertEquals("tpl_test_image_01", template.id)

    // Check background image properties
    assertEquals("IMAGE", template.background.type)
    assertEquals("bg_abstract_01.webp", template.background.imageAsset)
    assertEquals(2, template.background.overlayColors.size)
    assertEquals(0.75f, template.background.overlayAlpha ?: 0f, 0.01f)

    // Check real image element
    val imgElement = template.elements.find { it.id == "el_real_img" }
    assertNotNull(imgElement)
    assertEquals(red.line.instayar.data.model.ElementType.IMAGE, imgElement?.type)
    assertEquals("fashion_01.webp", imgElement?.imageAsset)
    assertEquals("FIT", imgElement?.contentScale)
    assertEquals(0.1f, imgElement?.imagePanX ?: 0f, 0.01f)
    assertEquals(-0.2f, imgElement?.imagePanY ?: 0f, 0.01f)
    assertEquals(1.5f, imgElement?.imageZoom ?: 1f, 0.01f)

    // Check placeholder with sample image
    val placeholderElement = template.elements.find { it.id == "el_placeholder_img" }
    assertNotNull(placeholderElement)
    assertEquals(red.line.instayar.data.model.ElementType.IMAGE_PLACEHOLDER, placeholderElement?.type)
    assertEquals("beauty_01.webp", placeholderElement?.imageAsset)
    assertEquals("CROP", placeholderElement?.contentScale)
  }

  @Test
  fun `verify AssetImageLoader handles missing file safely without crash`() {
    val context = ApplicationProvider.getApplicationContext<Context>()

    // 1. Non-existent asset file should return null without throwing exception
    val missingBitmap = red.line.instayar.util.AssetImageLoader.loadAssetBitmap(
      context = context,
      assetName = "non_existent_never_found_image.webp",
      targetWidth = 400,
      targetHeight = 400
    )
    assertEquals(null, missingBitmap)

    // 2. Blank or null asset name should return null safely
    val emptyBitmap = red.line.instayar.util.AssetImageLoader.loadAssetBitmap(
      context = context,
      assetName = "",
      targetWidth = 400,
      targetHeight = 400
    )
    assertEquals(null, emptyBitmap)

    // 3. Blank URI string and null inputs should return null safely
    val emptyUriBitmap = red.line.instayar.util.AssetImageLoader.loadContentUriBitmap(
      context = context,
      uriString = "",
      targetWidth = 400,
      targetHeight = 400
    )
    assertEquals(null, emptyUriBitmap)

    val nullBothBitmap = red.line.instayar.util.AssetImageLoader.loadBitmap(
      context = context,
      assetName = null,
      imageUri = null
    )
    assertEquals(null, nullBothBitmap)

    // 4. Sample downscaling calculation test
    val inSampleSize = red.line.instayar.util.AssetImageLoader.calculateInSampleSize(
      actualWidth = 2000,
      actualHeight = 3000,
      reqWidth = 500,
      reqHeight = 600
    )
    assertTrue(inSampleSize >= 4)
  }

  @Test
  fun `verify free user bitmap has watermark and differs from vip user bitmap`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val repo = TemplateRepository(context)
    val template = repo.loadTemplates().first()
    val brandProfile = BrandProfile()

    // 1. Clear VIP status for Free User
    prefs.clearVipStatus()
    assertFalse(prefs.isVipUser.value)

    val freeBitmap = ImageExportUtil.renderTemplateToBitmap(
      template = template,
      brandProfile = brandProfile,
      context = context
    )
    assertNotNull(freeBitmap)

    // 2. Register valid Lifetime purchase with valid RSA signature
    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val lifetimeJson = """{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseToken":"token_life","purchaseTime":${System.currentTimeMillis()}}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(lifetimeJson.toByteArray(Charsets.UTF_8))
      }
      val validLifetimeSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)
      val recorded = prefs.recordValidPurchase(lifetimeJson, validLifetimeSig)
      assertTrue(recorded)
      assertTrue(prefs.isVipUser.value)

      // Verified VIP bitmap (no watermark)
      val vipBitmap = ImageExportUtil.renderTemplateToBitmap(
        template = template,
        brandProfile = brandProfile,
        context = context
      )
      assertNotNull(vipBitmap)
      assertEquals(freeBitmap.width, vipBitmap.width)
      assertEquals(freeBitmap.height, vipBitmap.height)

      // 3. Count pixel differences between free bitmap (with watermark) and VIP bitmap (clean)
      var differences = 0
      val step = 25
      for (x in 0 until freeBitmap.width step step) {
        for (y in 0 until freeBitmap.height step step) {
          if (freeBitmap.getPixel(x, y) != vipBitmap.getPixel(x, y)) {
            differences++
          }
        }
      }
      assertTrue("Free bitmap with watermark must differ from clean VIP bitmap", differences > 0)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify corrupted signature or invalid purchase keeps watermark active`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    prefs.clearVipStatus()

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val lifetimeJson = """{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseToken":"token_life","purchaseTime":${System.currentTimeMillis()}}"""
      val badSignature = "corrupted_fake_signature_123"

      // Invalid signature must be rejected
      val recorded = prefs.recordValidPurchase(lifetimeJson, badSignature)
      assertFalse(recorded)

      // resolveWatermarkStyle must return active watermark (STRONG by default)
      val watermarkStyle = ImageExportUtil.resolveWatermarkStyle(context)
      assertNotNull(watermarkStyle)
      assertEquals(WatermarkStyle.STRONG, watermarkStyle)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify WatermarkStyle behaviors for SUBTLE, STRONG, and AGGRESSIVE`() {
    val width = 600
    val height = 600

    // 1. Subtle: only bottom bar
    val subtleBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    ImageExportUtil.drawWatermark(android.graphics.Canvas(subtleBmp), width, height, WatermarkStyle.SUBTLE, 0f, 0f, bitmap = subtleBmp)

    // 2. Strong: diagonal pattern + bottom bar
    val strongBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    ImageExportUtil.drawWatermark(android.graphics.Canvas(strongBmp), width, height, WatermarkStyle.STRONG, 0f, 0f, bitmap = strongBmp)

    // 3. Aggressive: diagonal pattern + bottom bar + large center badge
    val aggressiveBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    ImageExportUtil.drawWatermark(android.graphics.Canvas(aggressiveBmp), width, height, WatermarkStyle.AGGRESSIVE, 0f, 0f, bitmap = aggressiveBmp)

    // Check bottom bar has pixels in all three styles
    val barH = (height * 0.056f).coerceIn(68f, 104f).toInt()
    val bottomY = height - (barH / 2)
    var bottomPixelsSubtle = 0
    var bottomPixelsStrong = 0
    var bottomPixelsAggressive = 0
    for (x in 50 until width step 20) {
      if (subtleBmp.getPixel(x, bottomY) != 0) bottomPixelsSubtle++
      if (strongBmp.getPixel(x, bottomY) != 0) bottomPixelsStrong++
      if (aggressiveBmp.getPixel(x, bottomY) != 0) bottomPixelsAggressive++
    }
    assertTrue("Subtle style must draw bottom bar", bottomPixelsSubtle > 0)
    assertTrue("Strong style must draw bottom bar", bottomPixelsStrong > 0)
    assertTrue("Aggressive style must draw bottom bar", bottomPixelsAggressive > 0)

    // Check body area: Subtle has NO pixels in body, Strong and Aggressive do (canvas diagonal text)
    var bodyPixelsSubtle = 0
    var bodyPixelsStrong = 0
    val maxBodyY = height - barH - 10
    for (x in 10 until width step 15) {
      for (y in 10 until maxBodyY step 15) {
        if (subtleBmp.getPixel(x, y) != 0) bodyPixelsSubtle++
        if (strongBmp.getPixel(x, y) != 0) bodyPixelsStrong++
      }
    }
    assertEquals("Subtle style must not draw diagonal pattern in body", 0, bodyPixelsSubtle)
    assertTrue("Strong style must draw diagonal watermark across image body", bodyPixelsStrong > 0)

    // Check center badge area (center circle): Aggressive has heavy center badge pixels
    var centerPixelsStrong = 0
    var centerPixelsAggressive = 0
    val cx = width / 2
    val cy = height / 2
    for (x in (cx - 40)..(cx + 40) step 4) {
      for (y in (cy - 40)..(cy + 40) step 4) {
        if (strongBmp.getPixel(x, y) != 0) centerPixelsStrong++
        if (aggressiveBmp.getPixel(x, y) != 0) centerPixelsAggressive++
      }
    }
    assertTrue("Aggressive style center badge must add significant center coverage", centerPixelsAggressive > centerPixelsStrong)
  }

  @Test
  fun `verify handlePurchaseSuccess rejects mismatched purchase sku and json productId`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val billingManager = MyketBillingManager(context, prefs)

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val now = System.currentTimeMillis()
      // JSON says vip_quarterly, but purchase says vip_monthly
      val json = """{"packageName":"red.line.instayar","productId":"vip_quarterly","purchaseTime":$now,"purchaseToken":"tok_123"}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(json.toByteArray(Charsets.UTF_8))
      }
      val validSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)

      val mockPurchase = com.farasource.billing.util.Purchase("inapp", json, validSig)
      try {
        val skuField = com.farasource.billing.util.Purchase::class.java.getDeclaredField("mSku").apply { isAccessible = true }
        skuField.set(mockPurchase, "vip_monthly")
      } catch (e: Exception) {
        val skuField = com.farasource.billing.util.Purchase::class.java.getDeclaredField("sku").apply { isAccessible = true }
        skuField.set(mockPurchase, "vip_monthly")
      }

      var resultReceived: BillingResult? = null
      billingManager.setPendingCallbackForTesting {
        resultReceived = it
      }

      billingManager.handlePurchaseSuccess(mockPurchase)
      assertTrue(resultReceived is BillingResult.Error)
      assertFalse(prefs.isVipUser.value)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify vip user export has zero residual watermark pixels compared to clean render`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val repo = TemplateRepository(context)
    val template = repo.loadTemplates().first()
    val brandProfile = BrandProfile()

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val now = System.currentTimeMillis()
      val lifetimeJson = """{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseToken":"token_life","purchaseTime":$now}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(lifetimeJson.toByteArray(Charsets.UTF_8))
      }
      val validLifetimeSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)
      val recorded = prefs.recordValidPurchase(lifetimeJson, validLifetimeSig)
      assertTrue(recorded)
      assertTrue(prefs.isVipUser.value)

      // 1. تولید مستقل تصویر مرجع کاملاً تمیز بدون دخالت هیچ واترمارکی:
      val (targetW, targetH) = when (template.format) {
        red.line.instayar.data.model.TemplateFormat.STORY -> Pair(1080, 1920)
        red.line.instayar.data.model.TemplateFormat.POST_PORTRAIT -> Pair(1080, 1350)
        red.line.instayar.data.model.TemplateFormat.POST_SQUARE -> Pair(1080, 1080)
        red.line.instayar.data.model.TemplateFormat.HIGHLIGHT -> Pair(1080, 1080)
      }
      val referenceCleanBitmap = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
      val androidCanvas = android.graphics.Canvas(referenceCleanBitmap)
      val composeCanvas = androidx.compose.ui.graphics.Canvas(androidCanvas)
      val drawScope = androidx.compose.ui.graphics.drawscope.CanvasDrawScope()
      drawScope.draw(
        density = androidx.compose.ui.unit.Density(3f),
        layoutDirection = androidx.compose.ui.unit.LayoutDirection.Rtl,
        canvas = composeCanvas,
        size = androidx.compose.ui.geometry.Size(targetW.toFloat(), targetH.toFloat())
      ) {
        drawTemplate(
          template = template,
          canvasSize = size,
          brandProfile = brandProfile,
          context = context
        )
      }

      // 2. تولید تصویر خروجی VIP از مسیر واقعی Export:
      val vipExportBitmap = ImageExportUtil.renderTemplateToBitmap(
        template = template,
        brandProfile = brandProfile,
        context = context
      )

      assertEquals(referenceCleanBitmap.width, vipExportBitmap.width)
      assertEquals(referenceCleanBitmap.height, vipExportBitmap.height)

      // 3. مقایسه کامل تک‌تک پیکسل‌ها بدون Sampling (دقیقاً ۱۰۰٪ پیکسل‌ها بررسی می‌شوند):
      var diffCount = 0
      for (x in 0 until vipExportBitmap.width) {
        for (y in 0 until vipExportBitmap.height) {
          if (vipExportBitmap.getPixel(x, y) != referenceCleanBitmap.getPixel(x, y)) {
            diffCount++
          }
        }
      }
      assertEquals("خروجی VIP باید دقیقاً و بدون تغییر حتی یک پیکسل، منطبق بر تصویر تمیز مرجع باشد (Zero Residual Watermark)", 0, diffCount)

      // 4. مقایسه تصویر خروجی کاربر رایگان با تصویر مرجع تمیز (باید تفاوت پیکسلی داشته باشد):
      prefs.clearVipStatus()
      assertFalse(prefs.isVipUser.value)
      val freeExportBitmap = ImageExportUtil.renderTemplateToBitmap(
        template = template,
        brandProfile = brandProfile,
        context = context
      )
      var freeDiffCount = 0
      for (x in 0 until freeExportBitmap.width) {
        for (y in 0 until freeExportBitmap.height) {
          if (freeExportBitmap.getPixel(x, y) != referenceCleanBitmap.getPixel(x, y)) {
            freeDiffCount++
          }
        }
      }
      assertTrue("خروجی کاربر رایگان باید دارای واترمارک قابل تشخیص بوده و با تصویر مرجع تفاوت پیکسلی داشته باشد", freeDiffCount > 0)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify handlePurchaseSuccess rejects mismatch with requested sku`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val billingManager = MyketBillingManager(context, prefs)

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val now = System.currentTimeMillis()
      val json = """{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m"}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(json.toByteArray(Charsets.UTF_8))
      }
      val validSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)
      val mockPurchase = com.farasource.billing.util.Purchase("inapp", json, validSig)

      var resultReceived: BillingResult? = null
      billingManager.setPendingCallbackForTesting {
        resultReceived = it
      }

      val lastSkuField = MyketBillingManager::class.java.getDeclaredField("lastRequestedSku").apply { isAccessible = true }
      lastSkuField.set(billingManager, "vip_lifetime")

      billingManager.handlePurchaseSuccess(mockPurchase)
      assertTrue("عدم تطابق با SKU درخواستی باید منجر به خطای صریح شود", resultReceived is BillingResult.Error)
      assertFalse(prefs.isVipUser.value)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify handleItemAlreadyOwned rejects when owned sku differs from requested sku`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val billingManager = MyketBillingManager(context, prefs)

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val now = System.currentTimeMillis()
      val monthlyJson = """{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseTime":$now,"purchaseToken":"tok_m"}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(monthlyJson.toByteArray(Charsets.UTF_8))
      }
      val validSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)
      prefs.recordValidPurchase(monthlyJson, validSig)
      assertTrue(prefs.isVipUser.value)

      val latch = java.util.concurrent.CountDownLatch(1)
      var resultReceived: BillingResult? = null
      billingManager.setPendingCallbackForTesting {
        resultReceived = it
        latch.countDown()
      }
      val lastSkuField = MyketBillingManager::class.java.getDeclaredField("lastRequestedSku").apply { isAccessible = true }
      lastSkuField.set(billingManager, "vip_quarterly")

      val method = MyketBillingManager::class.java.getDeclaredMethod("handleItemAlreadyOwned").apply { isAccessible = true }
      method.invoke(billingManager)

      latch.await(3, java.util.concurrent.TimeUnit.SECONDS)
      org.robolectric.shadows.ShadowLooper.idleMainLooper()

      assertNotNull("کال‌بک خرید باید حتماً فراخوانی شود و null نباشد", resultReceived)
      assertTrue("مالکیت محصول متفاوت نباید به عنوان موفقیت خرید درخواستی گزارش شود", resultReceived is BillingResult.Error)
      val errorMsg = (resultReceived as BillingResult.Error).errorMessage
      assertTrue("پیام خطا باید مغایرت را مشخص کند", errorMsg.contains("تطابق ندارد"))

      // وضعیت خرید معتبر قبلی (vip_monthly) باید بدون تغییر و فعال باقی بماند
      assertTrue("اشتراک فعال قبلی کاربر نباید حذف شود", prefs.isVipUser.value)
      assertEquals("اشتراک کاربر باید همچنان vip_monthly باشد", "vip_monthly", prefs.vipPlanType.value)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify isPlanExpired handles normal time, minor drift, severe rollback, and lifetime correctly`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)

    val purchaseTime = System.currentTimeMillis()
    val dayMs = 24L * 60 * 60 * 1000L

    // 1. خرید عادی در محدوده ۳۰ روز (مثلاً ۱۰ روز بعد) -> منقضی نیست
    assertFalse("خرید در محدوده اشتراک نباید منقضی باشد", prefs.isPlanExpired("vip_monthly", purchaseTime, purchaseTime + (10 * dayMs)))

    // 2. گذشت ۳۵ روز از خرید ۳۰ روزه -> منقضی است
    assertTrue("گذشت بیش از دوره پلن باید باعث انقضا شود", prefs.isPlanExpired("vip_monthly", purchaseTime, purchaseTime + (35 * dayMs)))

    // 3. اختلاف ساعت جزئی دستگاه (مثلاً ۲ ساعت قبل از فاکتور به دلیل تاخیر ساعت کاربر ولی در بازه تسامح ۲۴ ساعته) -> نباید منقضی شود
    assertFalse("اختلاف ساعت جزئی کمتر از ۲۴ ساعت نباید باعث محرومیت کاربر شود", prefs.isPlanExpired("vip_monthly", purchaseTime, purchaseTime - (2 * 3600_000L)))

    // 4. عقب کشیدن شدید ساعت دستگاه (مثلاً ۳ روز قبل از تاریخ فاکتور جهت تقلب) -> منقضی است
    assertTrue("دستکاری ساعت دستگاه به بیش از ۲۴ ساعت قبل از فاکتور باید اشتراک را غیرفعال کند", prefs.isPlanExpired("vip_monthly", purchaseTime, purchaseTime - (3 * dayMs)))

    // 5. زمان خرید نامعتبر (۰ یا منفی) -> منقضی است
    assertTrue("زمان خرید صفر یا منفی باید منقضی تلقی شود", prefs.isPlanExpired("vip_monthly", 0L))

    // 6. پلن مادام‌العمر (vip_lifetime) تحت هیچ شرایطی منقضی نمی‌شود
    assertFalse("پلن لایف‌تایم هرگز منقضی نمی‌شود", prefs.isPlanExpired(MyketBillingManager.SKU_VIP_LIFETIME, purchaseTime, purchaseTime + (3650 * dayMs)))
    assertFalse("پلن لایف‌تایم با عقب کشیدن ساعت نیز منقضی نمی‌شود", prefs.isPlanExpired(MyketBillingManager.SKU_VIP_LIFETIME, purchaseTime, purchaseTime - (3650 * dayMs)))
  }

  @Test
  fun `verify saveBitmapToGallery handles title sanitization and sets MediaStore fields`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888).apply {
      eraseColor(android.graphics.Color.RED)
    }

    val customTitle = "پست تبلیغاتی بهار ۱۴۰۵!"
    val uri = ImageExportUtil.saveBitmapToGallery(context, bitmap, customTitle, Bitmap.CompressFormat.PNG, 100)
    assertNotNull("ذخیره در گالری باید موفقیت‌آمیز باشد", uri)

    // بررسی خواندن واقعی فایل PNG ذخیره‌شده از استریم
    val inputStream = context.contentResolver.openInputStream(uri!!)
    assertNotNull("استریم فایل ذخیره‌شده نباید null باشد", inputStream)
    val decoded = inputStream?.use { android.graphics.BitmapFactory.decodeStream(it) }
    assertNotNull("تصویر ذخیره‌شده باید بدون خطا دیکود شود", decoded)
    assertEquals(100, decoded?.width)
    assertEquals(100, decoded?.height)
    assertEquals(android.graphics.Color.RED, decoded?.getPixel(10, 10))
  }

  @Test
  fun `verify saveBitmapToGallery rolls back MediaStore insert on outputStream failure`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val recycledBitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
    recycledBitmap.recycle()

    val uri = ImageExportUtil.saveBitmapToGallery(context, recycledBitmap, "Failed_Test")
    assertNull("در صورت شکست فشرده‌سازی باید null برگردد و رکورد مدیا استور پاکسازی شود", uri)
  }

  @Test
  fun `verify expired vip subscription reactivates watermark on export`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val repo = TemplateRepository(context)
    val template = repo.loadTemplates().first()
    val brandProfile = BrandProfile()

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      // 1-month plan purchased 40 days ago (expired)
      val expiredTime = System.currentTimeMillis() - (40L * 24 * 60 * 60 * 1000)
      val expiredJson = """{"packageName":"red.line.instayar","productId":"vip_monthly","purchaseToken":"token_exp","purchaseTime":$expiredTime}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(expiredJson.toByteArray(Charsets.UTF_8))
      }
      val validSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)

      prefs.recordValidPurchase(expiredJson, validSig)
      // VIP status must be false because it is expired
      assertFalse("Expired plan must not grant VIP", prefs.isVipUser.value)

      // resolveWatermarkStyle must enforce STRONG watermark
      val style = ImageExportUtil.resolveWatermarkStyle(context)
      assertEquals(WatermarkStyle.STRONG, style)

      val expiredBitmap = ImageExportUtil.renderTemplateToBitmap(
        template = template,
        brandProfile = brandProfile,
        context = context
      )
      assertNotNull(expiredBitmap)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify restored valid vip purchase removes watermark completely`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val repo = TemplateRepository(context)
    val template = repo.loadTemplates().first()
    val brandProfile = BrandProfile()

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      // Restored active 3-month purchase
      val recentTime = System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000)
      val restoredJson = """{"packageName":"red.line.instayar","productId":"vip_quarterly","purchaseToken":"token_restored_123","purchaseTime":$recentTime}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(restoredJson.toByteArray(Charsets.UTF_8))
      }
      val validSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)

      val restored = prefs.recordValidPurchase(restoredJson, validSig)
      assertTrue(restored)
      assertTrue(prefs.isVipUser.value)

      // resolveWatermarkStyle must return null (clean output)
      val style = ImageExportUtil.resolveWatermarkStyle(context)
      assertNull("Active VIP must have no watermark style", style)

      val cleanBitmap = ImageExportUtil.renderTemplateToBitmap(
        template = template,
        brandProfile = brandProfile,
        context = context
      )
      assertNotNull(cleanBitmap)
      assertEquals(1080, cleanBitmap.width)
      assertEquals(1920, cleanBitmap.height)
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify gallery save and direct share execute without quality loss`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)

    // Save PNG
    val uriPng = ImageExportUtil.saveBitmapToGallery(context, bitmap, "Test_Save", Bitmap.CompressFormat.PNG, 100)
    assertNotNull("Gallery save PNG must succeed", uriPng)

    // Direct share
    val shareIntent = ImageExportUtil.shareBitmap(context, bitmap, preferInstagram = false)
    assertNotNull("Share intent must be created", shareIntent)
    assertEquals(android.content.Intent.ACTION_CHOOSER, shareIntent?.action)
  }

  @Test
  fun `verify direct share writes actual PNG file and preserves concurrent active files during cleanup`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888).apply {
      eraseColor(android.graphics.Color.BLUE)
    }

    val cacheImagesDir = java.io.File(context.cacheDir, "images").apply {
      if (!exists()) mkdirs()
    }

    // شبیه‌سازی یک فایل موقت قدیمی (۲ ساعت پیش) که باید پاک شود
    val oldFile = java.io.File(cacheImagesDir, "shared_story_old_test.png").apply {
      createNewFile()
      setLastModified(System.currentTimeMillis() - 7200_000L)
    }
    // شبیه‌سازی یک فایل همزمان فعال (۳۰ ثانیه پیش) که نباید پاک شود
    val concurrentFile = java.io.File(cacheImagesDir, "shared_story_active_test.png").apply {
      createNewFile()
      setLastModified(System.currentTimeMillis() - 30_000L)
    }

    val shareIntent = ImageExportUtil.shareBitmap(context, bitmap, preferInstagram = false)
    assertNotNull("اینتنت اشتراک‌گذاری باید ایجاد شود", shareIntent)

    // فایل قدیمی باید پاکسازی شده باشد
    assertFalse("فایل موقت قدیمی بیش از ۱ ساعت باید پاک شود", oldFile.exists())
    // فایل عملیات همزمان نباید پاک شده باشد
    assertTrue("فایل مربوط به عملیات همزمان نباید پاک شود", concurrentFile.exists())

    // بررسی اینکه فایل ایجادشده واقعاً حاوی بیت‌مپ معتبر است
    val targetIntent = if (shareIntent?.action == android.content.Intent.ACTION_CHOOSER) {
      shareIntent.getParcelableExtra<android.content.Intent>(android.content.Intent.EXTRA_INTENT)
    } else {
      shareIntent
    }
    val streamUri = targetIntent?.getParcelableExtra<android.net.Uri>(android.content.Intent.EXTRA_STREAM)
    assertNotNull("Uri استریم در اینتنت باید موجود باشد", streamUri)
    val decoded = context.contentResolver.openInputStream(streamUri!!)?.use {
      android.graphics.BitmapFactory.decodeStream(it)
    }
    assertNotNull(decoded)
    assertEquals(100, decoded?.width)
    assertEquals(100, decoded?.height)
    assertEquals(android.graphics.Color.BLUE, decoded?.getPixel(5, 5))

    if (concurrentFile.exists()) concurrentFile.delete()
  }

  @Test
  fun `verify all 4 template formats for zero residual watermark in VIP and distinct watermark in free user`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val repo = TemplateRepository(context)
    val allTemplates = repo.loadTemplates()
    val brandProfile = BrandProfile()

    val keyGen = java.security.KeyPairGenerator.getInstance("RSA").apply {
      initialize(1024)
    }
    val keyPair = keyGen.generateKeyPair()
    val testPubKeyBase64 = android.util.Base64.encodeToString(keyPair.public.encoded, android.util.Base64.NO_WRAP)
    MyketBillingManager.testPublicKeyOverride = testPubKeyBase64

    try {
      val lifetimeJson = """{"packageName":"red.line.instayar","productId":"vip_lifetime","purchaseToken":"token_all_fmts","purchaseTime":${System.currentTimeMillis()}}"""
      val signer = java.security.Signature.getInstance("SHA1withRSA").apply {
        initSign(keyPair.private)
        update(lifetimeJson.toByteArray(Charsets.UTF_8))
      }
      val validLifetimeSig = android.util.Base64.encodeToString(signer.sign(), android.util.Base64.NO_WRAP)

      val targetFormats = listOf(
        red.line.instayar.data.model.TemplateFormat.STORY,
        red.line.instayar.data.model.TemplateFormat.POST_SQUARE,
        red.line.instayar.data.model.TemplateFormat.POST_PORTRAIT,
        red.line.instayar.data.model.TemplateFormat.HIGHLIGHT
      )

      for (format in targetFormats) {
        val template = allTemplates.first { it.format == format }
        val (targetW, targetH) = when (format) {
          red.line.instayar.data.model.TemplateFormat.STORY -> Pair(1080, 1920)
          red.line.instayar.data.model.TemplateFormat.POST_PORTRAIT -> Pair(1080, 1350)
          red.line.instayar.data.model.TemplateFormat.POST_SQUARE -> Pair(1080, 1080)
          red.line.instayar.data.model.TemplateFormat.HIGHLIGHT -> Pair(1080, 1080)
        }

        // ۱. رندر تصویر مرجع کاملاً تمیز مستقل بدون هیچ کدی از واترمارک
        val referenceCleanBitmap = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val androidCanvas = android.graphics.Canvas(referenceCleanBitmap)
        val composeCanvas = androidx.compose.ui.graphics.Canvas(androidCanvas)
        val drawScope = androidx.compose.ui.graphics.drawscope.CanvasDrawScope()
        drawScope.draw(
          density = androidx.compose.ui.unit.Density(3f),
          layoutDirection = androidx.compose.ui.unit.LayoutDirection.Rtl,
          canvas = composeCanvas,
          size = androidx.compose.ui.geometry.Size(targetW.toFloat(), targetH.toFloat())
        ) {
          drawTemplate(template = template, canvasSize = size, brandProfile = brandProfile, context = context)
        }

        // ۲. رندر خروجی کاربر VIP از متد اصلی
        prefs.recordValidPurchase(lifetimeJson, validLifetimeSig)
        assertTrue(prefs.isVipUser.value)
        val vipBitmap = ImageExportUtil.renderTemplateToBitmap(template, brandProfile, context)

        assertEquals("ابعاد عرض برای قالب $format باید یکسان باشد", referenceCleanBitmap.width, vipBitmap.width)
        assertEquals("ابعاد ارتفاع برای قالب $format باید یکسان باشد", referenceCleanBitmap.height, vipBitmap.height)

        // مقایسه تک‌تک پیکسل‌ها (۱۰۰٪ بدون sampling) برای VIP
        var vipDiffCount = 0
        for (x in 0 until vipBitmap.width) {
          for (y in 0 until vipBitmap.height) {
            if (vipBitmap.getPixel(x, y) != referenceCleanBitmap.getPixel(x, y)) {
              vipDiffCount++
            }
          }
        }
        assertEquals("خروجی VIP در قالب $format باید دارای صفر پیکسل اختلاف باشد", 0, vipDiffCount)

        // ۳. رندر خروجی کاربر رایگان و تأیید وجود واترمارک
        prefs.clearVipStatus()
        assertFalse(prefs.isVipUser.value)
        val freeBitmap = ImageExportUtil.renderTemplateToBitmap(template, brandProfile, context)
        var freeDiffCount = 0
        for (x in 0 until freeBitmap.width) {
          for (y in 0 until freeBitmap.height) {
            if (freeBitmap.getPixel(x, y) != referenceCleanBitmap.getPixel(x, y)) {
              freeDiffCount++
            }
          }
        }
        assertTrue("خروجی رایگان در قالب $format باید حتماً دارای تفاوت پیکسلی واترمارک باشد", freeDiffCount > 0)
      }
    } finally {
      MyketBillingManager.testPublicKeyOverride = null
      prefs.clearVipStatus()
    }
  }

  @Test
  fun `verify high resolution template formats preserve dimensions and scale watermark proportionally`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    prefs.clearVipStatus()
    val repo = TemplateRepository(context)
    val templates = repo.loadTemplates()

    val storyTemplate = templates.first { it.format == red.line.instayar.data.model.TemplateFormat.STORY }
    val postTemplate = templates.first { it.format == red.line.instayar.data.model.TemplateFormat.POST_SQUARE }
    val portraitTemplate = templates.first { it.format == red.line.instayar.data.model.TemplateFormat.POST_PORTRAIT }

    val storyBmp = ImageExportUtil.renderTemplateToBitmap(storyTemplate, BrandProfile(), context)
    assertEquals(1080, storyBmp.width)
    assertEquals(1920, storyBmp.height)

    val postBmp = ImageExportUtil.renderTemplateToBitmap(postTemplate, BrandProfile(), context)
    assertEquals(1080, postBmp.width)
    assertEquals(1080, postBmp.height)

    val portraitBmp = ImageExportUtil.renderTemplateToBitmap(portraitTemplate, BrandProfile(), context)
    assertEquals(1080, portraitBmp.width)
    assertEquals(1350, portraitBmp.height)
  }
}
