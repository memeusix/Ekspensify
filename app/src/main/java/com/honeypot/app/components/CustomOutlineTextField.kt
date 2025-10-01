package com.honeypot.app.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.R
import com.honeypot.app.data.model.TextFieldStateModel
import com.honeypot.app.ui.theme.Red100
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun CustomOutlineTextField(
    state: MutableState<TextFieldStateModel>,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    disable: Boolean = false,
    radius: Dp = 16.dp,
    color: TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
    ),
    maxLength: Int = Int.MAX_VALUE,
    type: TextFieldType = TextFieldType.TEXT,
    isExpendable: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester = FocusRequester(),
    isNegativeAllow: Boolean = false,
    prefix: @Composable() (() -> Unit)? = null
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .let {
                if (disable) it.alpha(0.5f) else it
            }
    ) {
        OutlinedTextField(
            value = state.value.text,
            singleLine = !isExpendable,
            enabled = !disable,
            onValueChange = { input ->
                if (input.length <= maxLength) {
                    when (type) {
                        TextFieldType.NUMBER -> {
                            val filteredInput = input.filterIndexed { index, char ->
                                char.isDigit() || char == '.' || (isNegativeAllow && char == '-' && index == 0)
                            }
                            val isValidNegative =
                                !(filteredInput.count { it == '-' } > 1 || (filteredInput.contains(
                                    '-'
                                ) && filteredInput.first() != '-'))
                            if (isValidNegative && filteredInput.count { it == '.' } <= 1 && (filteredInput.isEmpty() || filteredInput.first() != '.')) {
                                state.value = state.value.copy(text = filteredInput, error = null)
                            }
                        }

                        else -> {
                            state.value = state.value.copy(text = input, error = null)
                        }
                    }
                }
            },
            placeholder = {
                Text(
                    placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            prefix = prefix,
            colors = color,
            suffix = {
                if (isPassword) {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }, modifier = Modifier.size(25.dp)) {
                        Icon(
                            painterResource(if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_close),
                            null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            },
            shape = RoundedCornerShape(radius),
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (state.value.isValid()) MaterialTheme.extendedColors.primaryBorder else Red100,
                    RoundedCornerShape(radius)
                )
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            keyboardOptions = keyboardOptions
        )
        state.value.error?.let {
            Text(
                text = it,
                color = Red100,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
    }
}

enum class TextFieldType {
    TEXT,
    NUMBER
}
