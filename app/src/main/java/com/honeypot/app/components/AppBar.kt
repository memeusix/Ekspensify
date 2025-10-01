package com.honeypot.app.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.honeypot.app.R
import com.honeypot.app.ui.theme.Typography
import com.honeypot.app.utils.singleClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    heading: String,
    navController: NavController,
    elevation: Boolean = false,
    isBackNavigation: Boolean = true,
    isLightColor: Boolean = false,
    actions: @Composable() (RowScope.() -> Unit) = {},
    bgColor: Color = MaterialTheme.colorScheme.background
) {
    val titleTextStyle = Typography.titleSmall.copy(
        fontWeight = FontWeight.SemiBold,
        color = if (isLightColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
    )

    val navigationIconTint =
        if (isLightColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground

    Surface(
        shadowElevation = if (elevation) 0.5.dp else 0.dp,
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = bgColor
            ),
            title = { Text(heading, style = titleTextStyle) },
            navigationIcon = {
                if (isBackNavigation) {
                    AppBarBackBtn(navController, navigationIconTint)
                }
            },
            actions = actions,
            expandedHeight = 60.dp,
        )
    }
}

@Composable
fun AppBarBackBtn(
    navController: NavController,
    navigationIconTint: Color = MaterialTheme.colorScheme.onBackground,
    size: Dp = Dp.Unspecified
) {
    IconButton(
        onClick = singleClick(onClick = navController::popBackStack),
        modifier = Modifier.padding(start = 5.dp).size(size)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back_arrow),
            contentDescription = "Back",
            tint = navigationIconTint
        )
    }
}