package com.honeypot.app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.honeypot.app.ui.categories.components.ShowIconLoader

@Composable
fun ShowBottomLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        ShowIconLoader(Modifier.size(32.dp))
    }
}
