package com.honeypot.app.ui.acounts.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastSumBy
import com.honeypot.app.R
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.utils.AccountType
import com.honeypot.app.utils.formatRupees
import java.math.BigDecimal

@Composable
fun AccountCardFooter(
    accountList: List<AccountResponseModel>,
    selectedAccountType: AccountType,
) {
    val filteredList = remember(accountList, selectedAccountType) {
        accountList.filter { it.type == selectedAccountType.toString() }
    }
    val totalBalance = remember(filteredList) {
        filteredList.sumOf { it.balance ?: BigDecimal.ZERO }
    }
    CustomListItem(
        title = stringResource(R.string.total),
        subtitle = "${filteredList.size} ${selectedAccountType.getDisplayName()} Accounts",
        modifier = Modifier.padding(10.dp),
        trailingContent = {
            AccountBalance(false, totalBalance.formatRupees())
        },
        enable = false,
        onClick = {}
    )
}