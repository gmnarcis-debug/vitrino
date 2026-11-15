package red.line.instayar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.data.model.ElementType
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateBackground
import red.line.instayar.data.model.TemplateElement
import red.line.instayar.data.model.TemplateFormat
import red.line.instayar.data.repository.TemplateRepository
import red.line.instayar.util.PersianDateUtil

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class TemplateFeaturesTest {

    // 1. Test In-Frame Image Crop, Scale and Offset
    @Test
    fun testImageInFrameCropAndScale() {
        val element = TemplateElement(
            id = "el_img_1",
            type = ElementType.IMAGE,
            x = 0.1f,
            y = 0.2f,
            width = 0.8f,
            height = 0.5f,
            imageAsset = "fashion_01.webp",
            imageScale = 1.8f,
            imageOffsetX = 0.25f,
            imageOffsetY = -0.15f
        )

        assertEquals(1.8f, element.effectiveScale, 0.001f)
        assertEquals(0.25f, element.effectiveOffsetX, 0.001f)
        assertEquals(-0.15f, element.effectiveOffsetY, 0.001f)
    }

    // 2. Test Brand Logo Element and Fallback
    @Test
    fun testBrandProfileLogo() {
        val profileWithoutLogo = BrandProfile(
            brandName = "کافه بهشت",
            logoUri = null
        )
        assertNull(profileWithoutLogo.logoUri)
        assertEquals("ک", profileWithoutLogo.brandName.take(1))

        val profileWithLogo = BrandProfile(
            brandName = "بوتیک گل سرخ",
            logoUri = "/data/user/0/red.line.instayar/files/brand_logo.png"
        )
        assertNotNull(profileWithLogo.logoUri)
        assertTrue(profileWithLogo.logoUri!!.endsWith(".png"))
    }

    // 3. Test Text Typography, Fonts, Outline, Opacity and LetterSpacing
    @Test
    fun testTextTypographyAndEffects() {
        val textElement = TemplateElement(
            id = "el_txt_1",
            type = ElementType.TEXT,
            x = 0.1f,
            y = 0.1f,
            width = 0.8f,
            height = 0.1f,
            text = "فروش ویژه پاییزه",
            fontFamily = "lalezar",
            outlineColor = "#000000",
            outlineWidth = 2.5f,
            opacity = 0.85f,
            letterSpacing = 0.05f
        )

        assertEquals("lalezar", textElement.fontFamily)
        assertEquals("#000000", textElement.outlineColor)
        assertEquals(2.5f, textElement.outlineWidth, 0.001f)
        assertEquals(0.85f, textElement.opacity, 0.001f)
        assertEquals(0.05f, textElement.letterSpacing, 0.001f)
    }

    // 4. Test Price Block Discount Calculation & Persian Numerals
    @Test
    fun testPriceBlockDiscountCalculation() {
        val priceBlock = TemplateElement(
            id = "el_price_1",
            type = ElementType.PRICE_BLOCK,
            x = 0.1f,
            y = 0.7f,
            width = 0.8f,
            height = 0.15f,
            oldPrice = 1000000L,
            newPrice = 750000L,
            currencyUnit = "تومان"
        )

        assertEquals(ElementType.PRICE_BLOCK, priceBlock.type)
        assertEquals(25, priceBlock.discountPercentage)

        val persianDiscount = PersianDateUtil.toPersianDigits("٪${priceBlock.discountPercentage}")
        assertEquals("٪۲۵", persianDiscount)

        // Test no discount when newPrice >= oldPrice
        val noDiscountBlock = priceBlock.copy(oldPrice = 500000L, newPrice = 500000L)
        assertEquals(0, noDiscountBlock.discountPercentage)
    }

    // 5. Test JSON Serialization & Deserialization of all new fields
    @Test
    fun testTemplateJsonSerializationWithNewFeatures() {
        val element = TemplateElement(
            id = "el_p1",
            type = ElementType.PRICE_BLOCK,
            x = 0.15f,
            y = 0.65f,
            width = 0.7f,
            height = 0.14f,
            oldPrice = 1200000L,
            newPrice = 840000L,
            currencyUnit = "تومان",
            fontFamily = "sahel",
            outlineColor = "#331100",
            outlineWidth = 1.5f,
            opacity = 0.95f,
            letterSpacing = 0.02f,
            imageScale = 1.4f,
            imageOffsetX = 0.1f,
            imageOffsetY = -0.2f
        )

        val template = Template(
            id = "tpl_test_new_features",
            title = "تست قابلیت‌های جدید",
            category = "تست",
            format = TemplateFormat.STORY,
            isVip = true,
            background = TemplateBackground(),
            elements = listOf(element)
        )

        // Context not needed for repository serialization/parsing
        val repo = TemplateRepository()
        val jsonString = repo.serializeTemplateToJson(template)

        assertNotNull(jsonString)
        assertTrue(jsonString.contains("PRICE_BLOCK"))
        assertTrue(jsonString.contains("sahel"))
        assertTrue(jsonString.contains("1200000"))
        assertTrue(jsonString.contains("840000"))

        val parsedTemplate = repo.parseTemplateJson(jsonString)
        assertEquals("tpl_test_new_features", parsedTemplate.id)
        assertEquals(1, parsedTemplate.elements.size)

        val parsedEl = parsedTemplate.elements.first()
        assertEquals(ElementType.PRICE_BLOCK, parsedEl.type)
        assertEquals("sahel", parsedEl.fontFamily)
        assertEquals(1200000L, parsedEl.oldPrice)
        assertEquals(840000L, parsedEl.newPrice)
        assertEquals(30, parsedEl.discountPercentage)
        assertEquals(1.5f, parsedEl.outlineWidth, 0.001f)
        assertEquals("#331100", parsedEl.outlineColor)
        assertEquals(0.95f, parsedEl.opacity, 0.001f)
        assertEquals(1.4f, parsedEl.imageScale, 0.001f)
        assertEquals(0.1f, parsedEl.imageOffsetX, 0.001f)
    }

    // 6. Test Geometric Element Signature Uniqueness and Safe Zones across all Templates
    @Test
    fun testTemplatesGeometricSignatureUniqueness() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        assertTrue("Must have templates loaded", templates.isNotEmpty())

        val signatures = mutableMapOf<String, String>()

        for (template in templates) {
            // Measure geometric element signature: type, shape, and rounded bounding geometry
            val signature = template.elements.joinToString(separator = "|") { el ->
                val rx = (el.x * 100).toInt()
                val ry = (el.y * 100).toInt()
                val rw = (el.width * 100).toInt()
                val rh = (el.height * 100).toInt()
                val shape = el.shapeType.uppercase()
                "${el.type}_${shape}_${rx}_${ry}_${rw}_${rh}_${el.rotation.toInt()}"
            }

            val duplicateOf = signatures[signature]
            assertNull(
                "Duplicate layout signature detected between template '${template.id}' and '$duplicateOf'! Each template must have a unique layout structure.",
                duplicateOf
            )
            signatures[signature] = template.id

            // Safe zone check
            if (template.format == TemplateFormat.STORY) {
                for (el in template.elements) {
                    if (el.type == ElementType.TEXT || el.type == ElementType.PRICE_BLOCK) {
                        assertTrue(
                            "Critical text in template '${template.id}' (${el.id}: '${el.text}') violates top 13% safe zone: y=${el.y} < 0.13",
                            el.y >= 0.13f - 0.001f
                        )
                        assertTrue(
                            "Critical text in template '${template.id}' (${el.id}: '${el.text}') violates bottom 20% safe zone: y+h=${el.y + el.height} > 0.815",
                            el.y + el.height <= 0.815f + 0.001f
                        )
                    }
                    if (el.type == ElementType.IMAGE) {
                        assertNotNull("Image element ${el.id} in template ${template.id} must specify an imageAsset", el.imageAsset)
                        assertTrue("Image element ${el.id} in template ${template.id} must point to a non-empty asset", el.imageAsset!!.isNotBlank())
                    }
                }
            } else if (template.format == TemplateFormat.POST_SQUARE) {
                for (el in template.elements) {
                    if (el.type == ElementType.TEXT || el.type == ElementType.PRICE_BLOCK) {
                        assertTrue(
                            "Critical text in post template '${template.id}' (${el.id}) violates left 6% margin: x=${el.x} < 0.06",
                            el.x >= 0.06f - 0.001f
                        )
                        assertTrue(
                            "Critical text in post template '${template.id}' (${el.id}) violates top 6% margin: y=${el.y} < 0.06",
                            el.y >= 0.06f - 0.001f
                        )
                        assertTrue(
                            "Critical text in post template '${template.id}' (${el.id}) violates right 6% margin: x+w=${el.x + el.width} > 0.94",
                            el.x + el.width <= 0.94f + 0.001f
                        )
                        assertTrue(
                            "Critical text in post template '${template.id}' (${el.id}) violates bottom 6% margin: y+h=${el.y + el.height} > 0.94",
                            el.y + el.height <= 0.94f + 0.001f
                        )
                    }
                    if (el.type == ElementType.IMAGE) {
                        assertNotNull("Image element ${el.id} in template ${template.id} must specify an imageAsset", el.imageAsset)
                        assertTrue("Image element ${el.id} in template ${template.id} must point to a non-empty asset", el.imageAsset!!.isNotBlank())
                    }
                }
            } else if (template.format == TemplateFormat.HIGHLIGHT) {
                for (el in template.elements) {
                    if (el.type == ElementType.TEXT) {
                        if (el.id.contains("title")) {
                            assertTrue(
                                "Title element in highlight template '${template.id}' (${el.id}) must have fontSize >= 22",
                                el.fontSize >= 22f - 0.001f
                            )
                        }
                        assertTrue(
                            "Highlight content in '${template.id}' (${el.id}) violates central bounds: x=${el.x} < 0.15",
                            el.x >= 0.15f - 0.001f
                        )
                        assertTrue(
                            "Highlight content in '${template.id}' (${el.id}) violates central bounds: y=${el.y} < 0.15",
                            el.y >= 0.15f - 0.001f
                        )
                        assertTrue(
                            "Highlight content in '${template.id}' (${el.id}) violates central bounds: x+w=${el.x + el.width} > 0.85",
                            el.x + el.width <= 0.85f + 0.001f
                        )
                        assertTrue(
                            "Highlight content in '${template.id}' (${el.id}) violates central bounds: y+h=${el.y + el.height} > 0.85",
                            el.y + el.height <= 0.85f + 0.001f
                        )
                    }
                }
            }
        }
    }

    // 7. Test Vertical Text Overlap Prevention across all templates
    @Test
    fun testNoTextElementOverlap() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        assertTrue(templates.isNotEmpty())

        for (template in templates) {
            val canvasH = if (template.format == TemplateFormat.STORY) 640f else 360f
            val textElements = template.elements.filter {
                (it.type == ElementType.TEXT || it.type == ElementType.PRICE_BLOCK) &&
                !it.text.isNullOrBlank() &&
                it.textColor != "#00000000" // ignore intentionally transparent outline background words
            }

            for (i in 0 until textElements.size) {
                val a = textElements[i]
                val aText = a.text ?: ""
                val aExplicitLines = aText.split("\n").size
                val aEstLines = kotlin.math.ceil((aText.length * a.fontSize * 0.6) / (a.width * 360.0) - 0.0001).toInt().coerceAtLeast(1)
                val aLines = maxOf(aExplicitLines, aEstLines)
                val aActualH = (a.fontSize * aLines * 1.7f) / canvasH
                val aTop = a.y
                val aBottom = a.y + aActualH
                val aLeft = a.x
                val aRight = a.x + a.width

                assertTrue(
                    "Text element '${a.id}' in template '${template.id}' actual height $aActualH exceeds element.height ${a.height}",
                    aActualH <= a.height + 0.005f
                )

                for (j in i + 1 until textElements.size) {
                    val b = textElements[j]
                    if (a.rotation != 0f || b.rotation != 0f) continue
                    val bText = b.text ?: ""
                    val bExplicitLines = bText.split("\n").size
                    val bEstLines = kotlin.math.ceil((bText.length * b.fontSize * 0.6) / (b.width * 360.0) - 0.0001).toInt().coerceAtLeast(1)
                    val bLines = maxOf(bExplicitLines, bEstLines)
                    val bActualH = (b.fontSize * bLines * 1.7f) / canvasH
                    val bTop = b.y
                    val bBottom = b.y + bActualH
                    val bLeft = b.x
                    val bRight = b.x + b.width

                    // Check horizontal intersection
                    val hOverlap = maxOf(aLeft, bLeft) < minOf(aRight, bRight)
                    // Check vertical intersection
                    val vOverlap = maxOf(aTop, bTop) < minOf(aBottom, bBottom)

                    if (hOverlap && vOverlap) {
                        org.junit.Assert.fail(
                            "Text overlap detected in template '${template.id}': " +
                            "Element '${a.id}' ('${a.text}', y=[$aTop, $aBottom]) overlaps with " +
                            "Element '${b.id}' ('${b.text}', y=[$bTop, $bBottom])"
                        )
                    }
                }
            }
        }
    }

    // 8. Test Standardized Template Categories
    @Test
    fun testTemplateCategories() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        val allowedCategories = setOf(
            "معرفی محصول",
            "تخفیف و حراج",
            "قیمت‌نامه",
            "اطلاعیه",
            "مناسبتی",
            "نظر مشتری",
            "تبریک",
            "سفارش‌گیری"
        )

        for (template in templates) {
            assertTrue(
                "Template '${template.id}' has invalid category '${template.category}'. Must be one of $allowedCategories",
                template.category in allowedCategories
            )
        }
    }

    // 9. Test WCAG 2.1 Text Contrast Ratio (>= 4.5:1)
    @Test
    fun testTextContrast() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        fun parseColor(hex: String): IntArray {
            val clean = hex.removePrefix("#")
            return when (clean.length) {
                6 -> intArrayOf(clean.substring(0, 2).toInt(16), clean.substring(2, 4).toInt(16), clean.substring(4, 6).toInt(16), 255)
                8 -> intArrayOf(clean.substring(2, 4).toInt(16), clean.substring(4, 6).toInt(16), clean.substring(6, 8).toInt(16), clean.substring(0, 2).toInt(16))
                else -> intArrayOf(0, 0, 0, 255)
            }
        }

        fun relLuminance(r: Int, g: Int, b: Int): Double {
            fun ch(v: Int): Double {
                val s = v / 255.0
                return if (s <= 0.04045) s / 12.92 else Math.pow((s + 0.055) / 1.055, 2.4)
            }
            return 0.2126 * ch(r) + 0.7152 * ch(g) + 0.0722 * ch(b)
        }

        fun blend(fg: String, bg: String): IntArray {
            val (r1, g1, b1, a1) = parseColor(fg)
            val (r2, g2, b2, _) = parseColor(bg)
            val alpha = a1 / 255.0
            val r = (r1 * alpha + r2 * (1 - alpha)).toInt()
            val g = (g1 * alpha + g2 * (1 - alpha)).toInt()
            val b = (b1 * alpha + b2 * (1 - alpha)).toInt()
            return intArrayOf(r, g, b, 255)
        }

        fun contrastRatio(c1: String, c2: String): Double {
            val (r1, g1, b1, _) = parseColor(c1)
            val (r2, g2, b2, _) = parseColor(c2)
            val l1 = relLuminance(r1, g1, b1)
            val l2 = relLuminance(r2, g2, b2)
            val lighter = maxOf(l1, l2)
            val darker = minOf(l1, l2)
            return (lighter + 0.05) / (darker + 0.05)
        }

        for (template in templates) {
            val bgType = template.background.type
            val bgColor = template.background.color

            for (idx in template.elements.indices) {
                val el = template.elements[idx]
                if ((el.type != ElementType.TEXT && el.type != ElementType.PRICE_BLOCK) || el.text.isNullOrBlank() || el.textColor == "#00000000") {
                    continue
                }

                var effectiveBg = el.backgroundColor
                if (effectiveBg.isNullOrBlank() || effectiveBg == "#00000000") {
                    // Check previous shapes under this element (Painter's algorithm in reverse)
                    for (prevIdx in (idx - 1) downTo 0) {
                        val prev = template.elements[prevIdx]
                        if (prev.type == ElementType.SHAPE && !prev.backgroundColor.isNullOrBlank() && prev.backgroundColor != "#00000000") {
                            val insideX = prev.x <= el.x + 0.02f && (prev.x + prev.width) >= (el.x + el.width) - 0.02f
                            val insideY = prev.y <= el.y + 0.02f && (prev.y + prev.height) >= (el.y + el.height) - 0.02f
                            if (insideX && insideY) {
                                effectiveBg = prev.backgroundColor
                                break
                            }
                        }
                    }
                }

                if (effectiveBg.isNullOrBlank() || effectiveBg == "#00000000") {
                    if (bgType != "IMAGE") {
                        effectiveBg = bgColor
                    } else {
                        continue
                    }
                }

                val bgParsed = parseColor(effectiveBg)
                val finalBgHex = if (bgParsed[3] < 255) {
                    val (br, bg, bb, _) = blend(effectiveBg, bgColor)
                    String.format("#%02X%02X%02X", br, bg, bb)
                } else effectiveBg

                val ratio = contrastRatio(el.textColor, finalBgHex)
                assertTrue(
                    "WCAG contrast failure in template '${template.id}' (${el.id}: '${el.text}'). " +
                    "Contrast ratio $ratio < 4.5 between textColor ${el.textColor} and background $finalBgHex",
                    ratio >= 4.5
                )
            }
        }
    }

    // 10. Test Every Template File in Assets Is Successfully Loaded
    @Test
    fun testEveryTemplateFileIsLoaded() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val assetManager = context.assets
        val assetFiles = assetManager.list("templates")?.filter { it.endsWith(".json") } ?: emptyList()
        assertTrue("Asset templates must not be empty", assetFiles.isNotEmpty())

        val repo = TemplateRepository(context)
        val loadedTemplates = repo.loadTemplates()

        assertEquals(
            "Every template file in assets/templates/ must be loaded without parse failures",
            assetFiles.size,
            loadedTemplates.size
        )

        val loadedIds = loadedTemplates.map { it.id }.toSet()
        for (fileName in assetFiles) {
            val expectedId = fileName.removeSuffix(".json")
            assertTrue("Template '$expectedId' from file '$fileName' must be present in loaded templates", loadedIds.contains(expectedId))
        }
    }

    // 11. Test VIP and Free Distribution per Format
    @Test
    fun testVipAndFreeTemplatesPerFormat() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        val expectedVipIds = setOf(
            "tpl_b1_02_diagonal",
            "tpl_b1_05_editorial",
            "tpl_b2_02_polaroid",
            "tpl_b2_05_hexagon",
            "tpl_b3_02_price_editorial",
            "tpl_b3_04_order_arch",
            "tpl_b4_01_yalda_ornamental",
            "tpl_b4_04_mothers_day_polaroid",
            "tpl_b5_02_review_hexagon",
            "tpl_b6_03_post_split_vertical",
            "tpl_b6_05_post_dominant_type",
            "tpl_b7_02_story_fathers_minimal",
            "tpl_b7_04_story_wedding_postcard",
            "tpl_b7_07_post_valentines_calligraphy",
            "tpl_b7_08_post_vertical_luxury_perfume",
            "tpl_b8_03_highlight_reviews",
            "tpl_b8_07_highlight_delivery",
            "tpl_b8_10_highlight_contact"
        )

        val storyTemplates = templates.filter { it.format == TemplateFormat.STORY }
        val postSquareTemplates = templates.filter { it.format == TemplateFormat.POST_SQUARE }
        val postPortraitTemplates = templates.filter { it.format == TemplateFormat.POST_PORTRAIT }
        val highlightTemplates = templates.filter { it.format == TemplateFormat.HIGHLIGHT }

        assertEquals(34, storyTemplates.size)
        assertEquals(9, postSquareTemplates.size)
        assertEquals(3, postPortraitTemplates.size)
        assertEquals(12, highlightTemplates.size)
        assertEquals(58, templates.size)

        // Require at least one VIP and at least one Free in each format
        assertTrue("Story must have at least one VIP template", storyTemplates.any { it.isVip })
        assertTrue("Story must have at least one Free template", storyTemplates.any { !it.isVip })
        assertTrue("Post Square must have at least one VIP template", postSquareTemplates.any { it.isVip })
        assertTrue("Post Square must have at least one Free template", postSquareTemplates.any { !it.isVip })
        assertTrue("Post Portrait must have at least one VIP template", postPortraitTemplates.any { it.isVip })
        assertTrue("Post Portrait must have at least one Free template", postPortraitTemplates.any { !it.isVip })
        assertTrue("Highlight must have at least one VIP template", highlightTemplates.any { it.isVip })
        assertTrue("Highlight must have at least one Free template", highlightTemplates.any { !it.isVip })

        for (template in templates) {
            val shouldBeVip = template.id in expectedVipIds
            assertEquals(
                "Template '${template.id}' VIP status mismatch",
                shouldBeVip,
                template.isVip
            )
        }

        val totalVip = templates.count { it.isVip }
        val totalFree = templates.count { !it.isVip }
        assertEquals(18, totalVip)
        assertEquals(40, totalFree)

        val storyVip = storyTemplates.count { it.isVip }
        val storyFree = storyTemplates.count { !it.isVip }
        val postSquareVip = postSquareTemplates.count { it.isVip }
        val postSquareFree = postSquareTemplates.count { !it.isVip }
        val postPortraitVip = postPortraitTemplates.count { it.isVip }
        val postPortraitFree = postPortraitTemplates.count { !it.isVip }
        val highlightVip = highlightTemplates.count { it.isVip }
        val highlightFree = highlightTemplates.count { !it.isVip }

        println("=== TEMPLATE LOAD SUMMARY ===")
        println("Total Loaded Templates: ${templates.size}")
        println("Story Format Templates: ${storyTemplates.size} (VIP: $storyVip, Free: $storyFree)")
        println("Post Square Format Templates: ${postSquareTemplates.size} (VIP: $postSquareVip, Free: $postSquareFree)")
        println("Post Portrait Format Templates: ${postPortraitTemplates.size} (VIP: $postPortraitVip, Free: $postPortraitFree)")
        println("Highlight Format Templates: ${highlightTemplates.size} (VIP: $highlightVip, Free: $highlightFree)")
        println("Total VIP: $totalVip, Total Free: $totalFree")

        val categoriesGroup = templates.groupBy { it.category }.mapValues { it.value.size }
        println("=== CATEGORY DISTRIBUTION ===")
        categoriesGroup.forEach { (cat, count) ->
            println("دسته '$cat': $count قالب")
        }
        println("=============================")
    }

    @Test
    fun testPostTemplatesFilteringCountAndIds() {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val allTemplates = kotlinx.coroutines.runBlocking { repo.loadTemplates() }

        // All post templates (format match: POST_PORTRAIT or POST_SQUARE)
        val postAll = allTemplates.filter {
            it.format == TemplateFormat.POST_PORTRAIT || it.format == TemplateFormat.POST_SQUARE
        }
        val postProduct = postAll.filter { it.category == "معرفی محصول" }
        val postCongratulation = postAll.filter { it.category == "تبریک" }
        val postDiscount = postAll.filter { it.category == "تخفیف و حراج" }

        println("=== POST FILTERING BREAKDOWN ===")
        println("N for Post with category 'همه': ${postAll.size}")
        println("Post IDs for 'همه': ${postAll.map { it.id }}")
        println("N for Post with category 'معرفی محصول': ${postProduct.size}")
        println("Post IDs for 'معرفی محصول': ${postProduct.map { it.id }}")
        println("N for Post with category 'تبریک': ${postCongratulation.size}")
        println("Post IDs for 'تبریک': ${postCongratulation.map { it.id }}")
        println("N for Post with category 'تخفیف و حراج': ${postDiscount.size}")
        println("Post IDs for 'تخفیف و حراج': ${postDiscount.map { it.id }}")
        println("================================")

        // When viewing format POST with category "همه", N is exactly 12
        assertEquals(12, postAll.size)
        assertTrue(postAll.any { it.id == "tpl_b6_01_post_grid4" })
        assertTrue(postAll.any { it.id == "tpl_b6_02_post_corner_circle" })
        assertTrue(postAll.any { it.id == "tpl_b6_03_post_split_vertical" })
        assertTrue(postAll.any { it.id == "tpl_b6_04_post_vintage_frame" })
        assertTrue(postAll.any { it.id == "tpl_b6_05_post_dominant_type" })
        assertTrue(postAll.any { it.id == "tpl_b6_06_post_diagonal_banner" })
        assertTrue(postAll.any { it.id == "tpl_b7_05_post_eid_fitr" })
        assertTrue(postAll.any { it.id == "tpl_b7_06_post_teachers_day" })
        assertTrue(postAll.any { it.id == "tpl_b7_07_post_valentines_calligraphy" })
        assertTrue(postAll.any { it.id == "tpl_b7_08_post_vertical_luxury_perfume" })
        assertTrue(postAll.any { it.id == "tpl_b7_09_post_vertical_leather_bag" })
        assertTrue(postAll.any { it.id == "tpl_b7_10_post_vertical_smart_audio" })

        // When category "معرفی محصول" is selected: 4 from b6 + 3 from b7 = 7
        assertEquals(7, postProduct.size)

        // When category "تبریک" is selected: 3 from b7
        assertEquals(3, postCongratulation.size)

        // When category "تخفیف و حراج" is selected: 2 from b6
        assertEquals(2, postDiscount.size)
    }

    // 13. Test No Duplicate Product Images across all templates
    @Test
    fun testNoDuplicateImages() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        val imageUsageMap = mutableMapOf<String, MutableList<String>>()

        for (template in templates) {
            for (element in template.elements) {
                if (element.type == ElementType.IMAGE && !element.imageAsset.isNullOrBlank()) {
                    val asset = element.imageAsset!!
                    // Only test product images, not background textures (bg_*)
                    if (!asset.startsWith("bg_")) {
                        imageUsageMap.getOrPut(asset) { mutableListOf() }.add(template.id)
                    }
                }
            }
        }

        println("=== PRODUCT IMAGE USAGE TABLE ===")
        println("| عکس (Asset) | قالب (Template) | تعداد استفاده |")
        println("| :--- | :--- | :--- |")
        for ((asset, templateList) in imageUsageMap.toSortedMap()) {
            println("| $asset | ${templateList.joinToString(", ")} | ${templateList.size} |")
        }
        println("=================================")

        val duplicates = imageUsageMap.filter { it.value.size > 1 }
        assertTrue(
            "Duplicate product images detected across templates: $duplicates",
            duplicates.isEmpty()
        )
    }

    // 14. Test No Empty Text on Elements with Background (or Badges) across all templates
    @Test
    fun testNoEmptyTextOnElementsWithBackground() = kotlinx.coroutines.runBlocking {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = TemplateRepository(context)
        val templates = repo.loadTemplates()

        assertEquals("Must load all 58 templates", 58, templates.size)

        var testedElementsCount = 0
        for (template in templates) {
            for (element in template.elements) {
                val hasBg = element.backgroundColor != null &&
                    element.backgroundColor != "TRANSPARENT" &&
                    element.backgroundColor != "#00000000" &&
                    element.backgroundColor.isNotBlank()

                if (element.type == ElementType.BADGE || (element.type == ElementType.TEXT && hasBg)) {
                    assertFalse(
                        "Element '${element.id}' in template '${template.id}' has background or is BADGE but text is null or blank!",
                        element.text.isNullOrBlank()
                    )
                    testedElementsCount++
                }
            }
        }

        // Verify tpl_b1_01_fullscreen specifically
        val tplB101 = templates.find { it.id == "tpl_b1_01_fullscreen" }
        assertNotNull("tpl_b1_01_fullscreen must exist", tplB101)

        val highlightBadge = tplB101!!.elements.find { it.id == "el_highlight_badge" }
        val highlightText = tplB101.elements.find { it.id == "el_highlight_text" }
        val pricePill = tplB101.elements.find { it.id == "el_price_pill" }
        val priceAmount = tplB101.elements.find { it.id == "el_price_amount" }

        assertNotNull(highlightBadge)
        assertNotNull(highlightText)
        assertNotNull(pricePill)
        assertNotNull(priceAmount)

        assertFalse("highlightText must not be blank", highlightText!!.text.isNullOrBlank())
        assertFalse("priceAmount must not be blank", priceAmount!!.text.isNullOrBlank())

        // Ensure text is vertically positioned inside or aligned with the badge
        assertTrue(
            "highlightText y (${highlightText.y}) must be within badge bounds [${highlightBadge!!.y}, ${highlightBadge.y + highlightBadge.height}]",
            highlightText.y >= highlightBadge.y && highlightText.y <= highlightBadge.y + highlightBadge.height
        )
        assertTrue(
            "priceAmount y (${priceAmount.y}) must be within pill bounds [${pricePill!!.y}, ${pricePill.y + pricePill.height}]",
            priceAmount.y >= pricePill.y && priceAmount.y <= pricePill.y + pricePill.height
        )

        println("=== TEST NO EMPTY TEXT ON ELEMENTS WITH BACKGROUND ===")
        println("Verified $testedElementsCount elements across 58 templates. All have valid, non-blank text.")
        println("======================================================")
    }
}

