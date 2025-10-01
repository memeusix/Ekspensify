package com.honeypot.app.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun CustomSpacer(
    height: Dp = Dp.Unspecified,
    width: Dp = Dp.Unspecified
) {
    Spacer(
        modifier = Modifier
            .let { if (height != Dp.Unspecified) it.height(height) else it }
            .let { if (width != Dp.Unspecified) it.width(width) else it }
    )
}

@Composable
fun VerticalSpace(height: Dp = Dp.Unspecified) = CustomSpacer(height = height)

@Composable
fun HorizontalSpace(width: Dp = Dp.Unspecified) = CustomSpacer(width = width)


