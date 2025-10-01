package com.honeypot.app.ui.dashboard.bottomNav.components

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.honeypot.app.R
import com.honeypot.app.ui.dashboard.bottomNav.BottomNavItem
import com.honeypot.app.utils.drawImageCutoutCenter


@Composable
fun BottomBarContent(
    items: List<BottomNavItem>,
    currentIndex: Int,
    onItemClick: (Int) -> Unit
) {

    val context = LocalContext.current
    val bitmap =
        AppCompatResources.getDrawable(context, R.drawable.ic_bottom_nav_cutout)!!.toBitmap()
    val imageBitmap = bitmap.asImageBitmap()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .drawImageCutoutCenter(imageBitmap)
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items.forEachIndexed { index, bottomNavItem ->
            if (bottomNavItem == BottomNavItem.Action) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                key(bottomNavItem.label) {
                    BottomNavOptions(
                        item = bottomNavItem,
                        isSelected = index == currentIndex,
                        modifier = Modifier.weight(1f),
                        onClick = { onItemClick(index) }
                    )
                }
            }
        }
    }
}
