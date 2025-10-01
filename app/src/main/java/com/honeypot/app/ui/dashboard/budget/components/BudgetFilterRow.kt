package com.honeypot.app.ui.dashboard.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.data.model.BudgetMeta
import com.honeypot.app.ui.theme.Violet40
import com.honeypot.app.utils.BudgetStatus
import com.honeypot.app.utils.roundedBorder

@Composable
fun BudgetFilterRow(
    modifier: Modifier,
    filterData: BudgetMeta?,
    onFilterClick: (BudgetStatus) -> Unit,
    onCreateClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BudgetStatus.entries.forEach { option ->
            BudgetFilterOption(
                title = option.displayName,
                count = when (option) {
                    BudgetStatus.RUNNING -> filterData?.running ?: 0
                    BudgetStatus.CLOSED -> filterData?.closed ?: 0
                }.toString(),
                color = option.color,
                onClick = { onFilterClick(option) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()

            )
        }
        CreateBudgetBtn(
            onClick = onCreateClick
        )
    }
}

@Composable
private fun BudgetFilterOption(
    modifier: Modifier,
    title: String,
    count: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .clickable(enabled = false, onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            count,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp
            )
        )
        Text(
            title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RowScope.CreateBudgetBtn(
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .roundedBorder(color = Violet40)
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        Icon(
            rememberAsyncImagePainter(R.drawable.ic_create_budget),
            contentDescription = "CreateBudget",
            tint = Violet40,
        )
        Text(
            "Create",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Violet40
            )
        )
    }
}

