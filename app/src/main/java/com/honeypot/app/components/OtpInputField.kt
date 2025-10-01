package com.honeypot.app.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.honeypot.app.ui.theme.Violet40
import com.honeypot.app.ui.theme.extendedColors
import kotlinx.coroutines.delay

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    shouldShowCursor: Boolean = false,
    shouldCursorBlink: Boolean = false,
    onOTPChanged: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpLength) {
            throw IllegalArgumentException("OTP should be $otpLength digits")
        }
    }
    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpLength) {
                onOTPChanged(it.text)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
            ) {
                repeat(otpLength) { index ->
                    CharacterContainer(
                        index = index,
                        text = otpText,
                        shouldShowCursor = shouldShowCursor,
                        shouldCursorBlink = shouldCursorBlink,
                        modifier = modifier.weight(1f)
                    )
                }

            }
        }
    )
}


@Composable
internal fun CharacterContainer(
    index: Int,
    text: String,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
    modifier: Modifier
) {
    val isFocused = text.length == index
    val character = when {
        index < text.length -> text[index].toString()
        else -> ""
    }


    // Cursor visibility state
    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    // Blinking effect for the cursor
    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(800) // Adjust the blinking speed here
                cursorVisible.value = !cursorVisible.value
            }
        }
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .height(50.dp)
//            .border(
//                width = when {
//                    isFocused -> 2.dp
//                    else -> 1.dp
//                },
//                color = when {
//                    isFocused -> MaterialTheme.colorScheme.primary
//                    else -> MaterialTheme.colorScheme.surfaceContainerHigh
//                },
//                shape = RoundedCornerShape(6.dp)
//            )
            .then(modifier)
    ) {
        if (character.isEmpty()) {
            val color =
                if (isFocused && cursorVisible.value) Violet40 else MaterialTheme.extendedColors.primaryBorder
            Canvas(
                modifier = Modifier.fillMaxSize(0.4f),
                onDraw = {
                    drawCircle(color = color)
                },
            )
        } else {
            Text(
                text = character,
                textAlign = TextAlign.Center,
                color =
                MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        // Display cursor when focused
//        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .width(2.dp)
//                    .height(24.dp) // Adjust height according to your design
//                    .background(Dark25)
//            )
//        }
    }
}