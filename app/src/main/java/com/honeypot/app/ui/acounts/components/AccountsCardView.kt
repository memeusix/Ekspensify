package com.honeypot.app.ui.acounts.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.honeypot.app.components.HorizontalDashedLine
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.AccountType

@Composable
fun AccountsCardView(
    selectedAccountType: AccountType,
    onTypeChange: (AccountType) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.extendedColors.primaryBorder, RoundedCornerShape(16.dp))
            .animateContentSize()
    ) {
        AccountTypeTabButton(
            selectedAccountType = selectedAccountType,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            onTypeChange = onTypeChange
        )
        HorizontalDashedLine()
        // Loading Content
        content()
    }
}