package com.honeypot.app.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.R
import com.honeypot.app.ui.theme.Green80
import com.honeypot.app.ui.theme.Light100
import com.honeypot.app.ui.theme.Red80
import kotlinx.coroutines.delay

@Composable
fun ColumnScope.OfflineText(isOffline: Boolean) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isOffline) {
        if (isOffline) {
            isVisible = true
        } else {
            delay(2000)
            isVisible = false
        }
    }

    val (text, color, icon) = remember(isOffline) {
        if (isOffline)
            Triple(
                "You Are Offline", Red80, R.drawable.ic_wifi_off
            )
        else
            Triple(
                "You are Online", Green80, R.drawable.ic_wifi_on
            )
    }

    AnimatedVisibility(
        visible = isVisible, modifier = Modifier.fillMaxWidth()
    ) {
        DrawableStartText(
            text = text,
            textSize = 12.sp,
            color = Light100,
            icon = icon,
            iconSize = 16.dp,
            iconSpace = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(vertical = 15.dp)
        )
    }
}


