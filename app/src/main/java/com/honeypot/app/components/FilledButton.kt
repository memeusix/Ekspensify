package com.honeypot.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
        fontWeight = FontWeight.SemiBold
    ),
    textModifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    enabled: Boolean = true
) {
    Button(
        shape = shape,
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, shape)

    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = textModifier
        )
    }
}
