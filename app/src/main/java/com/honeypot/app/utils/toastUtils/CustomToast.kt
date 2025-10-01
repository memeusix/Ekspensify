package com.honeypot.app.utils.toastUtils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.honeypot.app.ui.theme.Green120
import com.honeypot.app.ui.theme.Green20
import com.honeypot.app.ui.theme.Red120
import com.honeypot.app.ui.theme.Red20
import com.honeypot.app.ui.theme.Yellow120
import com.honeypot.app.ui.theme.Yellow20
import kotlinx.coroutines.delay

@Composable
fun CustomToast(toastState: MutableState<CustomToastModel?>) {

    LaunchedEffect(toastState.value?.isVisible) {
        delay(toastState.value?.duration ?: 1000)
        toastState.value = null
    }

    val alpha = animateFloatAsState(
        targetValue = if (toastState.value?.isVisible == true) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        ), label = ""
    )
    toastState.value?.let {
        AnimatedVisibility(
            visible = it.isVisible,
            modifier = Modifier
                .imePadding()
                .alpha(alpha.value)
                .fillMaxSize(),
        ) {
            Popup(
                alignment = Alignment.BottomCenter,
                offset = IntOffset(0, -200),
                onDismissRequest = { it.isVisible = false }
            ) {
                Text(
                    text = it.message
                        ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = when (it.type) {
                        ToastType.SUCCESS -> Green120
                        ToastType.INFO -> Yellow120
                        ToastType.WARNING -> Yellow120
                        ToastType.ERROR -> Red120
                    },
                    modifier = Modifier
                        .alpha(alpha.value)
                        .clip(RoundedCornerShape(20.dp))
                        .requiredWidthIn(max = LocalConfiguration.current.screenWidthDp.dp - 100.dp)
                        .background(
                            when (it.type) {
                                ToastType.SUCCESS -> Green20
                                ToastType.INFO -> Yellow20
                                ToastType.WARNING -> Yellow20
                                ToastType.ERROR -> Red20
                            },
                        )
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                )
            }
        }
    }
}
