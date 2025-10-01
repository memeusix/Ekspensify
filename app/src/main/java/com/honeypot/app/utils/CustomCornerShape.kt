package com.honeypot.app.utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class CustomCornerShape(
    private val topLeft: Dp = 0.dp,
    private val topRight: Dp = 0.dp,
    private val bottomLeft: Dp = 0.dp,
    private val bottomRight: Dp = 0.dp
) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): Outline {
        with(density) {
            val radii = listOf(
                topLeft.toPx(), topRight.toPx(),
                bottomLeft.toPx(), bottomRight.toPx()
            )
            return Outline.Rounded(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height),
                    topLeft = CornerRadius(radii[0], radii[0]),
                    topRight = CornerRadius(radii[1], radii[1]),
                    bottomLeft = CornerRadius(radii[2], radii[2]),
                    bottomRight = CornerRadius(radii[3], radii[3])
                )
            )
        }
    }
}