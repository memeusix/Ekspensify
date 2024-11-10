package com.memeusix.budgetbuddy.ui.dashboard.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.memeusix.budgetbuddy.ui.theme.Green100
import com.memeusix.budgetbuddy.ui.theme.Light100
import com.memeusix.budgetbuddy.ui.theme.Light60
import com.memeusix.budgetbuddy.ui.theme.Light80
import com.memeusix.budgetbuddy.ui.theme.Red100
import com.memeusix.budgetbuddy.ui.theme.Violet100

@Composable
fun IncomeExpenseCard(
    totalIncome: Long,
    totalExpenses: Long,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Light60,
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally)
        ) {
            IncomeExpenseItem(
                title = "Income",
                amount = totalIncome,
                color = Green100,
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .width(2.dp)
                    .background(Violet100)
            )
            IncomeExpenseItem(
                title = "Expense",
                amount = totalExpenses,
                color = Red100,
                modifier = Modifier.weight(1f)
            )
        }

    }
}


