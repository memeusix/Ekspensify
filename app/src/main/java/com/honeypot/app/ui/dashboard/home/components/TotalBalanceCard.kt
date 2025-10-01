package com.honeypot.app.ui.dashboard.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.DrawableStartText
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.ui.acounts.components.AmountText
import com.honeypot.app.ui.theme.Dark15
import com.honeypot.app.ui.theme.Light100
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun TotalBalanceCard(
    total: String,
    income: String,
    expense: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Dark15.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            VerticalSpace(10.dp)
            HeaderRow(onClick)
            AmountText(total, size = 32.sp, modifier = Modifier.padding(horizontal = 16.dp))
            VerticalSpace(16.dp)
            BalanceDetailsRow(
                income,
                expense
            )
        }
    }
}

@Composable
fun HeaderRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.total_balance),
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            rememberAsyncImagePainter(R.drawable.ic_arrow_right_long),
            contentDescription = "",
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

@Composable
fun BalanceDetailsRow(income: String, expense: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(IntrinsicSize.Min)
            .background(
                MaterialTheme.extendedColors.iconColor,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BalanceColumn(
            label = "Income",
            amount = income,
            iconResId = R.drawable.ic_income_arrow,
            alignment = Alignment.Start
        )
        VerticalDivider(thickness = 0.5.dp)
        BalanceColumn(
            label = "Expense",
            amount = expense,
            iconResId = R.drawable.ic_expense_arrow,
            alignment = Alignment.End
        )
    }
}


@Composable
fun BalanceColumn(
    label: String,
    amount: String,
    @DrawableRes iconResId: Int,
    alignment: Alignment.Horizontal
) {
    Column(
        horizontalAlignment = alignment
    ) {
        DrawableStartText(
            text = label,
            icon = iconResId,
            colorFilter = null,
            color = Light100,
            iconSize = 14.dp,
            textSize = 12.sp
        )
        VerticalSpace(8.dp)
        AmountText(amount, size = 18.sp, color = Light100)
    }
}