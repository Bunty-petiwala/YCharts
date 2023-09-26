package co.yml.charts.ui.linechart.model

import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.text.TextPaint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.getTextBackgroundRect
import co.yml.charts.common.model.Point

/**
 * SelectionHighlightPopUp is a data class used to draw the pop on the given selected point on a line
 * to identify the dimensions of the selected point.All the styling related params are included here
 * @param backgroundColor : Defines the background color of the popup.
 * @param backgroundAlpha : Defines the alpha of the background color.
 * @param backgroundCornerRadius : Defines the corner radius of the background.
 * @param backgroundColorFilter : ColorFilter to apply to the color when drawn into the destination.
 * @param backgroundBlendMode :Blending algorithm to be applied to the path when it is drawn.
 * @param backgroundStyle : Whether or not the path is stroked or filled in.
 * @param labelSize : The size of the popUp label in [TextUnit].
 * @param labelColor : The color of the label text.
 * @param labelAlignment : The alignment of the label text.
 * @param labelTypeface : The style of the label text.
 * @param paddingBetweenPopUpAndPoint : The padding between the anchor position/ popup
 * start position and the selected point on the line.
 * @param popUpLabel : The text that can be shown on the popup given 2 input params x and y values
 * @param draw : Draw the popUp marker on the selected point with 2 inputs [Offset] i.e selectedPoint
 * and [Point] i.e the input data w.r.t selected point
 */
data class SelectionHighlightPopUp(
    val backgroundColor: Color = Color.Black,
    val backgroundAlpha: Float = 0.7f,
    val backgroundCornerRadius: CornerRadius = CornerRadius(5f),
    val backgroundColorFilter: ColorFilter? = null,
    val backgroundBlendMode: BlendMode = DrawScope.DefaultBlendMode,
    val backgroundStyle: DrawStyle = Fill,
    val paddingBetweenPopUpAndPoint: Dp = 20.dp,
    val labelSize: TextUnit = 14.sp,
    val labelColor: Color = Color.White,
    val labelAlignment: Paint.Align = Paint.Align.CENTER,
    val labelTypeface: Typeface = Typeface.DEFAULT,
    val popUpLabel: (Float, Float) -> (String) = { x, y ->
        val xLabel = "x : ${x.toInt()} "
        val yLabel = "y : ${String.format("%.2f", y)}"
        "$xLabel $yLabel"
    },
    val draw: DrawScope.(Offset, Point) -> Unit = { selectedOffset, identifiedPoint ->
        val highlightTextPaint = TextPaint().apply {
            textSize = labelSize.toPx()
            color = labelColor.toArgb()
            textAlign = labelAlignment
            typeface = labelTypeface
        }
        var label = popUpLabel(identifiedPoint.x, identifiedPoint.y)
        var pos = identifiedPoint.x.toDouble()
        //if Pos is 0 then move little left

        val paddingBetweenPopUpAndPoint = 10.dp.toPx() // Adjust the padding as needed

        val background = getTextBackgroundRect(
            selectedOffset.x + if (pos == 0.0) 100f else 10f,
            selectedOffset.y - 80f,
            label,
            highlightTextPaint
        )

        val arrowSize = 20.dp.toPx() // Adjust the arrow size as needed
        val arrowX = background.centerX() // Center the arrow horizontally
        val arrowY = background.bottom // Align the arrow to the bottom of the background

        drawContext.canvas.nativeCanvas.apply {

            // Draw the rounded rectangle background
            drawRoundRect(
                color = backgroundColor,
                topLeft = Offset(
                    background.left.toFloat(),
                    background.top.toFloat() - paddingBetweenPopUpAndPoint
                ),
                size = Size(background.width().toFloat(), background.height() + arrowSize + 20),
                alpha = 1f,
                cornerRadius = backgroundCornerRadius,
                colorFilter = backgroundColorFilter,
                blendMode = backgroundBlendMode,
                style = backgroundStyle
            )

            val arrowPaint = TextPaint().apply {
                color = backgroundColor.toArgb()
            }

            // Draw the down arrow below the background
            val arrowPath = createArrowPath(arrowX.toFloat() +5f, arrowY.toFloat() - 10f, arrowSize)
            drawPath(arrowPath, arrowPaint)

            // Draw the text label
            //if contains \n shows in next line
            drawIntoCanvas { canvas ->
                val lines = label.split("\n")
                var yOffset = selectedOffset.y - paddingBetweenPopUpAndPoint - 50f
                var index = 0
                for (line in lines) {
                    canvas.nativeCanvas.drawText(
                        line,
                        selectedOffset.x + if (pos == 0.0) 120f else 0f,
                        yOffset,
                        highlightTextPaint
                    )
                    yOffset += highlightTextPaint.textSize + if (index == 1) 50f else 0f
                    index++
                }
            }
        }
    }
)


private fun createArrowPath(x: Float, y: Float, size: Float): android.graphics.Path {
    val path = Path()
    path.moveTo(x - size / 2, y)
    path.lineTo(x + size / 2, y)
    path.lineTo(x, y + size)
    path.close()
    return path
}