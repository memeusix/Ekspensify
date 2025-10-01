package com.honeypot.app.ui.acounts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.honeypot.app.ui.acounts.data.BankModel
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun AccountTypeGridItem(
    item: BankModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val (borderColor, backgroundColor) = if (item.isSelected) {
        MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.secondary
    } else {
        Color.Transparent to MaterialTheme.extendedColors.imageBg
    }
    
    Image(
        painterResource(item.icon!!),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = backgroundColor)
            .border(width = 1.dp, color = borderColor, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp)
            .clickable(
                enabled = item.isEnable,
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .alpha(if (item.isEnable) 1f else 0.1f),
    )
}

