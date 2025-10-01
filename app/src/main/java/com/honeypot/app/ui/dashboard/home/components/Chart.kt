package com.honeypot.app.ui.dashboard.home.components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.honeypot.app.data.model.responseModel.CategoryInsightsResponseModel
import com.honeypot.app.ui.theme.EmptyGrey
import com.honeypot.app.utils.getColor
import java.math.BigDecimal

@Composable
fun Chart(items: List<CategoryInsightsResponseModel>) {

    val animatedSweepAngles =
        items.map { item ->
            animateFloatAsState(
                targetValue = ((item.amount ?: BigDecimal.ZERO).toFloat() / items.sumOf { it.amount ?: BigDecimal.ZERO }
                    .toFloat()) * 360f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                label = ""
            )
        }

    Canvas(
        modifier = Modifier
            .size(150.dp)
            .aspectRatio(1f)
    ) {
        drawIntoCanvas {
            val width = size.width
            val radius = width / 2f
            val strokeWidth = radius * .6f
            var startAngle = 0f

            val total = items.sumOf { it.amount ?: BigDecimal.ZERO }
            if (items.isEmpty()) {
                drawArc(
                    color = EmptyGrey,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(width - strokeWidth, width - strokeWidth),
                    style = Stroke(
                        strokeWidth,
                        cap = StrokeCap.Butt,
                        pathEffect = PathEffect.cornerPathEffect(10f),
                        join = StrokeJoin.Round
                    )
                )
            } else {
                items.forEachIndexed { index, item->
//                    val sweepAngle = ((item.amount ?: 0.0).toFloat() / total).toFloat() * 360f
                    val sweepAngle = animatedSweepAngles[index].value
                    val gap = if (items.size == 1) 0f else 2f
                    drawArc(
                        color = getColor(item.category?.icFillColor),
                        startAngle = startAngle + gap,
                        sweepAngle = sweepAngle - gap * 2,
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(width - strokeWidth, width - strokeWidth),
                        style = Stroke(
                            strokeWidth,
                            cap = StrokeCap.Butt,
                            pathEffect = PathEffect.cornerPathEffect(10f),
                            join = StrokeJoin.Round
                        )
                    )
                    startAngle += sweepAngle
                }
            }
        }
    }
}

