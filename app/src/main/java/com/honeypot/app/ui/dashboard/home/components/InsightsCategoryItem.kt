package com.honeypot.app.ui.dashboard.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.components.HorizontalSpace
import com.honeypot.app.data.model.responseModel.CategoryInsightsResponseModel
import com.honeypot.app.ui.acounts.components.AmountText
import com.honeypot.app.utils.CustomCornerShape
import com.honeypot.app.utils.getColor

@Composable
fun InsightsCategoryItem(item: CategoryInsightsResponseModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 25.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            Modifier
                .fillMaxHeight()
                .width(3.dp)
                .clip(
                    CustomCornerShape(
                        topRight = 20.dp,
                        bottomRight = 20.dp
                    )
                )
                .background(getColor(item.category?.icFillColor)),
        )
        HorizontalSpace(8.dp)
        Text(
            item.category?.name ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = getColor(item.category?.icFillColor)
            )
        )
        Spacer(Modifier.weight(1f))
        AmountText(
            (item.amount ?: 0).toString(),
            size = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

