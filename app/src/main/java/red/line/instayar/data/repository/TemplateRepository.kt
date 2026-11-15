package red.line.instayar.data.repository

import android.content.Context
import red.line.instayar.data.model.ElementType
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateBackground
import red.line.instayar.data.model.TemplateElement
import red.line.instayar.data.model.TemplateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class TemplateRepository(private val context: Context? = null) {

    companion object {
        @Volatile
        private var memoryCachedTemplates: List<Template>? = null
        private val templateMap = java.util.concurrent.ConcurrentHashMap<String, Template>()
    }

    private val _templates = MutableStateFlow<List<Template>>(emptyList())
    val templates: StateFlow<List<Template>> = _templates.asStateFlow()

    suspend fun getTemplateById(templateId: String): Template? = withContext(Dispatchers.IO) {
        templateMap[templateId]?.let { return@withContext it }
        val cached = memoryCachedTemplates
        if (cached != null) {
            val found = cached.find { it.id == templateId }
            if (found != null) {
                templateMap[templateId] = found
                return@withContext found
            }
        }
        val currentContext = context ?: return@withContext null
        try {
            val assetManager = currentContext.assets
            // Look for matching json file directly
            val targetFile = "templates/$templateId.json"
            try {
                val inputStream = assetManager.open(targetFile)
                val content = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                val parsed = parseTemplateJson(content)
                templateMap[templateId] = parsed
                return@withContext parsed
            } catch (_: Exception) {
                // If filename does not match id directly, search files
                val files = assetManager.list("templates") ?: emptyArray()
                for (fileName in files) {
                    if (fileName.endsWith(".json")) {
                        val inputStream = assetManager.open("templates/$fileName")
                        val content = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                        val tpl = parseTemplateJson(content)
                        templateMap[tpl.id] = tpl
                        if (tpl.id == templateId) {
                            return@withContext tpl
                        }
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("TemplateRepository", "Error loading template $templateId", e)
        }
        null
    }

    suspend fun loadTemplates(forceRefresh: Boolean = false): List<Template> = withContext(Dispatchers.IO) {
        if (!forceRefresh && memoryCachedTemplates != null) {
            val cached = memoryCachedTemplates!!
            _templates.value = cached
            return@withContext cached
        }

        val loadedList = mutableListOf<Template>()
        val currentContext = context ?: return@withContext emptyList()
        try {
            val assetManager = currentContext.assets
            val files = assetManager.list("templates") ?: emptyArray()
            
            for (fileName in files) {
                if (fileName.endsWith(".json")) {
                    try {
                        val inputStream = assetManager.open("templates/$fileName")
                        val content = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                        val template = parseTemplateJson(content)
                        loadedList.add(template)
                        templateMap[template.id] = template
                    } catch (e: Exception) {
                        android.util.Log.e("TemplateRepository", "Error parsing template file: $fileName", e)
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("TemplateRepository", "Error accessing assets/templates directory", e)
        }

        // Sort: Free and VIP nicely mixed, or by ID
        val result = loadedList.sortedBy { it.id }
        memoryCachedTemplates = result
        _templates.value = result
        result
    }

    fun parseTemplateJson(jsonString: String): Template {
        val json = JSONObject(jsonString)
        val id = json.optString("id", "tpl_${System.currentTimeMillis()}")
        val title = json.optString("title", "قالب بدون نام")
        val category = json.optString("category", "عمومی")
        val formatStr = json.optString("format", "STORY")
        val format = TemplateFormat.fromString(formatStr)
        val isVip = json.optBoolean("isVip", false)

        val tags = mutableListOf<String>()
        val tagsArray = json.optJSONArray("tags")
        if (tagsArray != null) {
            for (i in 0 until tagsArray.length()) {
                tags.add(tagsArray.getString(i))
            }
        }

        val bgJson = json.optJSONObject("background")
        val bgType = bgJson?.optString("type", "GRADIENT") ?: "GRADIENT"
        val bgColor = bgJson?.optString("color", "#1A102F") ?: "#1A102F"
        val bgAngle = bgJson?.optDouble("angle", 45.0)?.toFloat() ?: 45f

        val gradientColors = mutableListOf<String>()
        val gradientArray = bgJson?.optJSONArray("gradientColors")
        if (gradientArray != null && gradientArray.length() > 0) {
            for (i in 0 until gradientArray.length()) {
                gradientColors.add(gradientArray.getString(i))
            }
        } else {
            gradientColors.add("#2E0854")
            gradientColors.add("#0C071E")
        }

        val bgImage = if (bgJson?.has("imageAsset") == true) bgJson.optString("imageAsset") else null
        val bgOverlayAlpha = bgJson?.optDouble("overlayAlpha", 0.6)?.toFloat() ?: 0.6f
        val overlayColors = mutableListOf<String>()
        val overlayArray = bgJson?.optJSONArray("overlayColors")
        if (overlayArray != null && overlayArray.length() > 0) {
            for (i in 0 until overlayArray.length()) {
                overlayColors.add(overlayArray.getString(i))
            }
        } else {
            overlayColors.add("#80000000")
            overlayColors.add("#D0000000")
        }

        val background = TemplateBackground(
            type = bgType,
            color = bgColor,
            gradientColors = gradientColors,
            angle = bgAngle,
            imageAsset = bgImage,
            overlayColors = overlayColors,
            overlayAlpha = bgOverlayAlpha
        )

        val elements = mutableListOf<TemplateElement>()
        val elementsArray = json.optJSONArray("elements") ?: JSONArray()
        for (i in 0 until elementsArray.length()) {
            val elObj = elementsArray.getJSONObject(i)
            val elId = elObj.optString("id", "el_$i")
            val typeStr = elObj.optString("type", "TEXT")
            val type = ElementType.fromString(typeStr)

            val x = elObj.optDouble("x", 0.0).toFloat()
            val y = elObj.optDouble("y", 0.0).toFloat()
            val width = elObj.optDouble("width", 0.5).toFloat()
            val height = elObj.optDouble("height", 0.2).toFloat()
            val rotation = elObj.optDouble("rotation", 0.0).toFloat()

            val text = if (elObj.has("text")) elObj.optString("text") else null
            val fontSize = elObj.optDouble("fontSize", 16.0).toFloat()
            val fontWeight = elObj.optString("fontWeight", "BOLD")
            val textColor = elObj.optString("textColor", "#FFFFFF")
            val textAlign = elObj.optString("textAlign", "CENTER")
            val backgroundColor = if (elObj.has("backgroundColor")) elObj.optString("backgroundColor") else null
            val shapeType = elObj.optString("shapeType", "ROUNDED_RECT")
            val cornerRadius = elObj.optDouble("cornerRadius", 16.0).toFloat()
            val borderColor = if (elObj.has("borderColor")) elObj.optString("borderColor") else null
            val borderWidth = elObj.optDouble("borderWidth", 0.0).toFloat()
            val badgeIcon = if (elObj.has("badgeIcon")) elObj.optString("badgeIcon") else null
            val placeholderLabel = if (elObj.has("placeholderLabel")) elObj.optString("placeholderLabel") else null
            val hasShadow = elObj.optBoolean("hasShadow", false)
            val shadowColor = elObj.optString("shadowColor", "#80000000")

            val imageAsset = if (elObj.has("imageAsset")) elObj.optString("imageAsset") else null
            val contentScale = elObj.optString("contentScale", "CROP")
            val imageUri = if (elObj.has("imageUri")) elObj.optString("imageUri") else null
            val imagePanX = elObj.optDouble("imagePanX", 0.0).toFloat()
            val imagePanY = elObj.optDouble("imagePanY", 0.0).toFloat()
            val imageZoom = elObj.optDouble("imageZoom", 1.0).toFloat()

            // New fields
            val imageScale = elObj.optDouble("imageScale", imageZoom.toDouble()).toFloat()
            val imageOffsetX = elObj.optDouble("imageOffsetX", imagePanX.toDouble()).toFloat()
            val imageOffsetY = elObj.optDouble("imageOffsetY", imagePanY.toDouble()).toFloat()

            val fontFamily = elObj.optString("fontFamily", "vazirmatn")
            val outlineColor = if (elObj.has("outlineColor")) elObj.optString("outlineColor") else null
            val outlineWidth = elObj.optDouble("outlineWidth", 0.0).toFloat()
            val opacity = elObj.optDouble("opacity", 1.0).toFloat()
            val letterSpacing = elObj.optDouble("letterSpacing", 0.0).toFloat()

            val oldPrice = if (elObj.has("oldPrice") && !elObj.isNull("oldPrice")) elObj.optLong("oldPrice") else null
            val newPrice = if (elObj.has("newPrice") && !elObj.isNull("newPrice")) elObj.optLong("newPrice") else null
            val currencyUnit = elObj.optString("currencyUnit", "تومان")

            elements.add(
                TemplateElement(
                    id = elId,
                    type = type,
                    x = x,
                    y = y,
                    width = width,
                    height = height,
                    rotation = rotation,
                    text = text,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    textColor = textColor,
                    textAlign = textAlign,
                    backgroundColor = backgroundColor,
                    shapeType = shapeType,
                    cornerRadius = cornerRadius,
                    borderColor = borderColor,
                    borderWidth = borderWidth,
                    badgeIcon = badgeIcon,
                    placeholderLabel = placeholderLabel,
                    hasShadow = hasShadow,
                    shadowColor = shadowColor,
                    imageAsset = imageAsset,
                    contentScale = contentScale,
                    imageUri = imageUri,
                    imagePanX = imagePanX,
                    imagePanY = imagePanY,
                    imageZoom = imageZoom,
                    imageScale = imageScale,
                    imageOffsetX = imageOffsetX,
                    imageOffsetY = imageOffsetY,
                    fontFamily = fontFamily,
                    outlineColor = outlineColor,
                    outlineWidth = outlineWidth,
                    opacity = opacity,
                    letterSpacing = letterSpacing,
                    oldPrice = oldPrice,
                    newPrice = newPrice,
                    currencyUnit = currencyUnit
                )
            )
        }

        return Template(
            id = id,
            title = title,
            category = category,
            format = format,
            isVip = isVip,
            tags = tags,
            background = background,
            elements = elements
        )
    }

    fun serializeTemplateToJson(template: Template): String {
        val root = JSONObject()
        root.put("id", template.id)
        root.put("title", template.title)
        root.put("category", template.category)
        root.put("format", template.format.name)
        root.put("isVip", template.isVip)

        val tagsArray = JSONArray()
        template.tags.forEach { tagsArray.put(it) }
        root.put("tags", tagsArray)

        val bgObj = JSONObject()
        bgObj.put("type", template.background.type)
        bgObj.put("color", template.background.color)
        bgObj.put("angle", template.background.angle)
        template.background.imageAsset?.let { bgObj.put("imageAsset", it) }
        bgObj.put("overlayAlpha", template.background.overlayAlpha)
        val gradArray = JSONArray()
        template.background.gradientColors.forEach { gradArray.put(it) }
        bgObj.put("gradientColors", gradArray)
        val overlayArray = JSONArray()
        template.background.overlayColors.forEach { overlayArray.put(it) }
        bgObj.put("overlayColors", overlayArray)
        root.put("background", bgObj)

        val elArray = JSONArray()
        template.elements.forEach { el ->
            val obj = JSONObject()
            obj.put("id", el.id)
            obj.put("type", el.type.name)
            obj.put("x", el.x)
            obj.put("y", el.y)
            obj.put("width", el.width)
            obj.put("height", el.height)
            obj.put("rotation", el.rotation)
            el.text?.let { obj.put("text", it) }
            obj.put("fontSize", el.fontSize)
            obj.put("fontWeight", el.fontWeight)
            obj.put("textColor", el.textColor)
            obj.put("textAlign", el.textAlign)
            el.backgroundColor?.let { obj.put("backgroundColor", it) }
            obj.put("shapeType", el.shapeType)
            obj.put("cornerRadius", el.cornerRadius)
            el.borderColor?.let { obj.put("borderColor", it) }
            obj.put("borderWidth", el.borderWidth)
            el.badgeIcon?.let { obj.put("badgeIcon", it) }
            el.placeholderLabel?.let { obj.put("placeholderLabel", it) }
            obj.put("hasShadow", el.hasShadow)
            obj.put("shadowColor", el.shadowColor)
            el.imageAsset?.let { obj.put("imageAsset", it) }
            obj.put("contentScale", el.contentScale)
            el.imageUri?.let { obj.put("imageUri", it) }
            obj.put("imagePanX", el.imagePanX)
            obj.put("imagePanY", el.imagePanY)
            obj.put("imageZoom", el.imageZoom)
            obj.put("imageScale", el.imageScale)
            obj.put("imageOffsetX", el.imageOffsetX)
            obj.put("imageOffsetY", el.imageOffsetY)
            el.fontFamily?.let { obj.put("fontFamily", it) }
            el.outlineColor?.let { obj.put("outlineColor", it) }
            obj.put("outlineWidth", el.outlineWidth)
            obj.put("opacity", el.opacity)
            obj.put("letterSpacing", el.letterSpacing)
            el.oldPrice?.let { obj.put("oldPrice", it) }
            el.newPrice?.let { obj.put("newPrice", it) }
            obj.put("currencyUnit", el.currencyUnit)
            elArray.put(obj)
        }
        root.put("elements", elArray)

        return root.toString()
    }
}
