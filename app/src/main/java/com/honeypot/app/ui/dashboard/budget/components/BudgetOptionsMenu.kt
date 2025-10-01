package com.honeypot.app.ui.dashboard.budget.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun BudgetOptionsMenu(
    isClosed: Boolean = false,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    BudgetDeleteBtn { isMenuOpen = true }

    DropdownMenu(
        expanded = isMenuOpen,
        onDismissRequest = { isMenuOpen = false },
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.extendedColors.primaryBorder),
        offset = DpOffset((-20).dp, Dp.Unspecified),
        modifier = modifier
    ) {
        if (!isClosed) {
            MenuItem(
                text = stringResource(R.string.close_budget),
                iconRes = R.drawable.ic_cancel_circle_half_dot,
                onClick = {
                    onCloseClick()
                    isMenuOpen = false
                },
            )
        }
        MenuItem(
            text = stringResource(R.string.delete_budget),
            iconRes = R.drawable.ic_delete,
            onClick = {
                onDeleteClick()
                isMenuOpen = false
            },
        )
    }
}

@Composable
private fun BudgetDeleteBtn(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(end = 5.dp)
    ) {
        Icon(
            rememberAsyncImagePainter(R.drawable.ic_menu_more),
            contentDescription = "Delete",
            tint = MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .size(24.dp)
                .padding(2.dp)
        )
    }
}

@Composable
private fun MenuItem(
    text: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)) },
        onClick = onClick,
        leadingIcon = {
            Icon(
                rememberAsyncImagePainter(iconRes),
                contentDescription = text,
                tint = MaterialTheme.extendedColors.iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
    )
}
