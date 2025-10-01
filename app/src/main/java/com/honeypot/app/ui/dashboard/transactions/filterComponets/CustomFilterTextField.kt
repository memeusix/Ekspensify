package com.honeypot.app.ui.dashboard.transactions.filterComponets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.ui.theme.extendedColors


@Composable
fun CustomFilterTextField(
    state: String,
    onChange: (String) -> Unit,
    isEnable: Boolean,
    isDisabled: Boolean = false,
    placeHolder: String,
    @DrawableRes leadingIcon: Int? = null
) {
    BasicTextField(
        value = state,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.extendedColors.primaryBorder, RoundedCornerShape(10.dp))
            .let {
                if (!isEnable) it.clickable(
                    enabled = !isDisabled,
                    onClick = { onChange("") }
                ) else it
            }
            .padding(12.dp)
            .alpha(if (isDisabled) 0.6f else 1f),
        onValueChange = { if (it.length <= 6) onChange(it) },
        maxLines = 1,
        enabled = isEnable,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        decorationBox = { innerBox ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(IntrinsicSize.Min)
            ) {
                leadingIcon?.let {
                    Icon(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                ) {
                    if (state.isEmpty()) {
                        Text(
                            text = placeHolder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    innerBox()
                }
            }

        }
    )
}