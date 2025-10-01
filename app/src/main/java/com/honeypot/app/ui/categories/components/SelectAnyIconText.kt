package com.honeypot.app.ui.categories.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.honeypot.app.R

@Composable
fun SelectAnyIconText(modifier: Modifier) {
    Text(
        stringResource(R.string.select_any_relevant_category_icon),
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
    )
}