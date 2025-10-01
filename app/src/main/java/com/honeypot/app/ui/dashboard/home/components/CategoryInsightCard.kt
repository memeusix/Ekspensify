package com.honeypot.app.ui.dashboard.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.DrawableEndText
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.model.requestModel.InsightsQueryModel
import com.honeypot.app.data.model.responseModel.CategoryInsightsResponseModel
import com.honeypot.app.ui.dashboard.transactions.data.getFormattedDateRange
import com.honeypot.app.ui.theme.Dark15
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.DateFormat
import com.honeypot.app.utils.formatZonedDateTime
import com.honeypot.app.utils.roundedBorder

@Composable
fun CategoryInsightCard(
    selectedType: String,
    onClick: () -> Unit,
    query: InsightsQueryModel,
    categoryInsights: List<CategoryInsightsResponseModel>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Dark15.copy(alpha = 0.6f))
            .padding(16.dp)
            .animateContentSize()
    ) {
        Column {
            CategoryHeaderRow(
                selectedType = selectedType,
                onClick = onClick
            )
            VerticalSpace(10.dp)
            PieChartRow(categoryInsights, query)
        }
    }
}

@Composable
private fun CategoryHeaderRow(selectedType: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            stringResource(R.string.breakdown),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
            )
        )
        DrawableEndText(
            text = selectedType,
            icon = R.drawable.ic_arrow_down,
            iconSize = 22.dp,
            textSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .fillMaxHeight()
                .roundedBorder(40.dp)
                .clip(RoundedCornerShape(40))
                .clickable(onClick = onClick)
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}


@Composable
private fun PieChartRow(
    categoryList: List<CategoryInsightsResponseModel>,
    query: InsightsQueryModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (categoryList.isEmpty()) {
            Column(
                Modifier.size(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    rememberAsyncImagePainter(R.drawable.img_empty_chart),
                    contentDescription = stringResource(R.string.empty_image_desc),
                )
                VerticalSpace(15.dp)
                Text(stringResource(R.string.no_data_found), style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Chart(categoryList)
        }
        StartEndDateColumn(query)
    }
}

@Composable
private fun StartEndDateColumn(query: InsightsQueryModel) {
    val (startDateTime, endDateTime) = query.dateRange.getFormattedDateRange()
    Column(
        horizontalAlignment = Alignment.End,
    ) {
        DateLabel(stringResource(R.string.from_label), startDateTime.formatZonedDateTime(DateFormat.dd_MMM_yyyy_))
        VerticalSpace(20.dp)
        DateLabel(stringResource(R.string.to_label), endDateTime.formatZonedDateTime(DateFormat.dd_MMM_yyyy_))
    }
}

@Composable
private fun DateLabel(label: String, date: String) {
    Text(
        label,
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
    VerticalSpace(8.dp)
    Text(
        date,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium
        ),
    )
}