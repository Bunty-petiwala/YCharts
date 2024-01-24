package co.yml.charts.ui.linechart.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Represents a point on the line graph
 *
 * @param color The color or fill to be applied to the circle
 * @param radius The radius of the circle
 * @param alpha Opacity to be applied to the circle from 0.0f to 1.0f representing
 * fully transparent to fully opaque respectively
 * @param style Whether or not the circle is stroked or filled in
 * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
 * @param blendMode Blending algorithm to be applied to the brush
 * @param draw override this to change the default drawCircle implementation. You are provided
 * with the actual [Point] that represents the intersection.
 */
data class IntersectionPoint(
    val color: Color = Color.Black,
    val radius: Dp = 6.dp,
    val alpha: Float = 1.0f,
    val style: DrawStyle = Fill,
    val colorFilter: ColorFilter? = null,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode,
    val draw: DrawScope.(Offset) -> Unit = { center ->

        val centerX = center.x
        val centerY = center.y

        val circleCenter = Offset(centerX , centerY)

        drawCircle(
            color = color,
            radius = radius.toPx(),
            center = circleCenter,
            alpha = alpha,
            style = style,
            colorFilter = colorFilter,
            blendMode = blendMode
        )

        val colorCircle = Color.White
        drawCircle(
            color = colorCircle,
            radius = (radius - 1.5.dp).toPx(),
            center = circleCenter,
            alpha = alpha,
            style = style,
            colorFilter = colorFilter,
            blendMode = blendMode
        )
    }
)

