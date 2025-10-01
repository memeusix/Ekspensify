package com.honeypot.app.ui.dashboard.bottomNav.components


import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.ui.theme.Green100
import com.honeypot.app.ui.theme.Red100
import com.honeypot.app.utils.TransactionType
import com.honeypot.app.utils.singleClick

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpandableFab(
    isFabExpanded: Boolean,
    onClick: (TransactionType) -> Unit
) {

    AnimatedVisibility(
        visible = isFabExpanded,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }) + fadeIn() + scaleIn(),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }) + fadeOut() + scaleOut(),

        modifier = Modifier
            .wrapContentSize(Alignment.BottomCenter)
            .zIndex(1f)
    ) {
        val horizontalSpacing = 40.dp
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                horizontalSpacing,
                Alignment.CenterHorizontally
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            ActionBtnItem(
                bgColor = Red100,
                icon = R.drawable.ic_expense,
                onClick = singleClick { onClick(TransactionType.DEBIT) })
            ActionBtnItem(
                bgColor = Green100,
                icon = R.drawable.ic_income,
                onClick = singleClick { onClick(TransactionType.CREDIT) })
        }
    }
}

@Composable
fun ActionBtnItem(bgColor: Color, @DrawableRes icon: Int, onClick: () -> Unit) {
    val iconSize = 55.dp
    val paddingSize = 10.dp
    Box(
        modifier = Modifier
            .size(iconSize)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            rememberAsyncImagePainter(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingSize)
        )
    }
}
