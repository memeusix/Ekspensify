package com.honeypot.app.ui.dashboard.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.R
import com.honeypot.app.components.DrawableStartText
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.ui.acounts.components.AmountText
import com.honeypot.app.ui.acounts.data.BankModel
import com.honeypot.app.ui.theme.Dark15

@Composable
fun HorizontalAccountListItem(account: AccountResponseModel) {
    val icon =
        BankModel.getAccounts().find { it.iconSlug == account.icon }?.icon ?: R.drawable.ic_wallet
    Box(
        modifier = Modifier
            .sizeIn(minWidth = 150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Dark15.copy(alpha = 0.6f))
            .padding(16.dp)
    ) {
        Column {
            DrawableStartText(
                text = account.name ?: "",
                icon = icon,
                colorFilter = null,
                textSize = 14.sp,
                fontWeight = FontWeight.Medium,
                iconSpace = 8.dp,
                iconSize = 16.dp
            )
            VerticalSpace(10.dp)
            Text(
                "Available Balance",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp
                )
            )
            VerticalSpace(2.dp)
            AmountText(account.balance.toString(), size = 20.sp)
        }
    }
}

