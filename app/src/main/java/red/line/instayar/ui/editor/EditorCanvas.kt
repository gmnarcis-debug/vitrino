package red.line.instayar.ui.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import red.line.instayar.data.model.BrandProfile
import red.line.instayar.data.model.ElementType
import red.line.instayar.data.model.Template
import red.line.instayar.data.model.TemplateElement
import red.line.instayar.ui.components.drawTemplate

@Composable
fun EditorInteractiveCanvas(
    template: Template,
    selectedElementId: String?,
    brandProfile: BrandProfile,
    isAdjustingInsideFrame: Boolean = false,
    onSelectElement: (String?) -> Unit,
    onDragElement: (dxRel: Float, dyRel: Float) -> Unit,
    onTransformElement: (scaleFactor: Float, deltaRotation: Float) -> Unit,
    onPanZoomInsideFrame: (panDx: Float, panDy: Float, zoomFactor: Float) -> Unit = { _, _, _ -> },
    onGestureEnd: () -> Unit,
    onRequestPickImage: ((elementId: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxW = maxWidth
        val maxH = maxHeight
        val aspect = template.aspectRatioFloat

        val canvasModifier = if (maxW / maxH > aspect) {
            Modifier
                .aspectRatio(aspect)
                .fillMaxSize()
        } else {
            Modifier
                .aspectRatio(aspect)
                .fillMaxSize()
        }

        Box(
            modifier = canvasModifier
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = Color(0x667C5CFF),
                    ambientColor = Color(0x44000000)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0B0B10))
                .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(20.dp))
                .testTag("editor_canvas_viewport")
        ) {
            // Interactive gesture layer
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(template.elements, selectedElementId) {
                        detectTapGestures(
                            onTap = { tapOffset ->
                                val canvasW = size.width.toFloat()
                                val canvasH = size.height.toFloat()
                                val relX = tapOffset.x / canvasW
                                val relY = tapOffset.y / canvasH

                                // Hit test in reverse order (top layer first)
                                val clickedElement = template.elements.reversed().find { el ->
                                    relX in el.x..(el.x + el.width) &&
                                            relY in el.y..(el.y + el.height)
                                }
                                onSelectElement(clickedElement?.id)
                            },
                            onDoubleTap = { tapOffset ->
                                val canvasW = size.width.toFloat()
                                val canvasH = size.height.toFloat()
                                val relX = tapOffset.x / canvasW
                                val relY = tapOffset.y / canvasH

                                val clickedElement = template.elements.reversed().find { el ->
                                    relX in el.x..(el.x + el.width) &&
                                            relY in el.y..(el.y + el.height)
                                }
                                if (clickedElement != null && (clickedElement.type == ElementType.IMAGE || clickedElement.type == ElementType.IMAGE_PLACEHOLDER)) {
                                    onSelectElement(clickedElement.id)
                                    onRequestPickImage?.invoke(clickedElement.id)
                                }
                            }
                        )
                    }
                    .pointerInput(selectedElementId, isAdjustingInsideFrame) {
                        forEachGesture {
                            awaitPointerEventScope {
                                var hasChanged = false
                                do {
                                    val event = awaitPointerEvent()
                                    val canceled = event.changes.any { it.isConsumed }
                                    if (canceled) break

                                    val zoomChange = event.calculateZoom()
                                    val rotationChange = event.calculateRotation()
                                    val panChange = event.calculatePan()

                                    if (zoomChange != 1f || rotationChange != 0f || panChange != androidx.compose.ui.geometry.Offset.Zero) {
                                        val canvasW = size.width.toFloat()
                                        val canvasH = size.height.toFloat()
                                        if (canvasW > 0 && canvasH > 0) {
                                            val selectedEl = template.elements.find { it.id == selectedElementId }
                                            val isImageEl = selectedEl != null && (selectedEl.type == ElementType.IMAGE || selectedEl.type == ElementType.IMAGE_PLACEHOLDER)

                                            if (isAdjustingInsideFrame && isImageEl) {
                                                val panDx = panChange.x / (canvasW * selectedEl!!.width)
                                                val panDy = panChange.y / (canvasH * selectedEl.height)
                                                onPanZoomInsideFrame(panDx, panDy, zoomChange)
                                                hasChanged = true
                                            } else {
                                                val dxRel = panChange.x / canvasW
                                                val dyRel = panChange.y / canvasH
                                                if (dxRel != 0f || dyRel != 0f) {
                                                    onDragElement(dxRel, dyRel)
                                                    hasChanged = true
                                                }
                                                if (zoomChange != 1f || rotationChange != 0f) {
                                                    onTransformElement(zoomChange, rotationChange)
                                                    hasChanged = true
                                                }
                                            }
                                        }
                                        event.changes.forEach { it.consume() }
                                    }
                                } while (event.changes.any { it.pressed })

                                if (hasChanged) {
                                    onGestureEnd()
                                }
                            }
                        }
                    }
            ) {
                // 1. Draw Template content
                drawTemplate(
                    template = template,
                    canvasSize = size,
                    brandProfile = brandProfile,
                    context = context
                )

                // 2. Draw active selection bounding box if an element is selected
                selectedElementId?.let { id ->
                    val selectedEl = template.elements.find { it.id == id }
                    if (selectedEl != null) {
                        drawSelectionBox(selectedEl, size, isAdjustingInsideFrame)
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSelectionBox(
    element: TemplateElement,
    canvasSize: Size,
    isAdjustingInsideFrame: Boolean = false
) {
    val canvasW = canvasSize.width
    val canvasH = canvasSize.height

    val x = element.x * canvasW
    val y = element.y * canvasH
    val w = element.width * canvasW
    val h = element.height * canvasH
    val padding = 6f

    val cx = x + w / 2f
    val cy = y + h / 2f

    val isImage = element.type == ElementType.IMAGE || element.type == ElementType.IMAGE_PLACEHOLDER
    val frameColor = if (isAdjustingInsideFrame && isImage) Color(0xFFF59E0B) else Color(0xFF06B6D4)

    rotate(degrees = element.rotation, pivot = Offset(cx, cy)) {
        val rectTopLeft = Offset(x - padding, y - padding)
        val rectSize = Size(w + padding * 2f, h + padding * 2f)

        // Glowing selection frame
        drawRoundRect(
            color = frameColor,
            topLeft = rectTopLeft,
            size = rectSize,
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(
                width = if (isAdjustingInsideFrame && isImage) 3.5f else 2.5f,
                pathEffect = PathEffect.dashPathEffect(
                    if (isAdjustingInsideFrame && isImage) floatArrayOf(8f, 6f) else floatArrayOf(12f, 8f),
                    0f
                )
            )
        )

        // Corner handles
        val handleRadius = 7f
        val corners = listOf(
            rectTopLeft,
            Offset(rectTopLeft.x + rectSize.width, rectTopLeft.y),
            Offset(rectTopLeft.x, rectTopLeft.y + rectSize.height),
            Offset(rectTopLeft.x + rectSize.width, rectTopLeft.y + rectSize.height)
        )

        for (corner in corners) {
            drawCircle(
                color = frameColor,
                radius = handleRadius,
                center = corner
            )
            drawCircle(
                color = Color.White,
                radius = handleRadius * 0.5f,
                center = corner
            )
        }

        // Top rotation stem & handle
        if (!isAdjustingInsideFrame) {
            val stemTop = Offset(cx, rectTopLeft.y - 20f)
            drawLine(
                color = frameColor,
                start = Offset(cx, rectTopLeft.y),
                end = stemTop,
                strokeWidth = 2f
            )
            drawCircle(
                color = Color(0xFFEC4899),
                radius = handleRadius * 1.1f,
                center = stemTop
            )
        }
    }
}
