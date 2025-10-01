package com.honeypot.app.ui.acounts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.honeypot.app.utils.AccountType

@Composable
fun AccountTypeTabButton(
    selectedAccountType: AccountType,
    modifier: Modifier,
    onTypeChange: (AccountType) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AccountType.entries.forEach { type ->
            CustomToggleButton(
                text = type.getDisplayName(),
                isSelected = type == selectedAccountType,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = { onTypeChange(type) }),
            )
        }
    }
}
