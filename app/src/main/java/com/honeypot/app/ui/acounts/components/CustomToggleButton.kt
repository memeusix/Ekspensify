package com.honeypot.app.ui.acounts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomToggleButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    fontStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    val (textColor, background) = if (isSelected) {
        MaterialTheme.colorScheme.onSecondary to MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onBackground to Color.Transparent
    }
    Text(
        text = text,
        color = textColor,
        textAlign = TextAlign.Center,
        style = fontStyle,
        modifier = Modifier
            .background(
                color = background,
                shape = shape
            )
            .then(modifier)

    )
}

