package com.memeusix.ekspensify.ui.dashboard.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memeusix.ekspensify.R
import com.memeusix.ekspensify.components.DrawableStartText
import com.memeusix.ekspensify.components.VerticalSpace
import com.memeusix.ekspensify.data.model.responseModel.BudgetResponseModel
import com.memeusix.ekspensify.ui.dashboard.budget.helper.getColor
import com.memeusix.ekspensify.ui.dashboard.budget.helper.getOnGoingText
import com.memeusix.ekspensify.ui.dashboard.budget.helper.getPeriod
import com.memeusix.ekspensify.ui.dashboard.budget.helper.getProgressValue
import com.memeusix.ekspensify.ui.dashboard.budget.helper.getStartEndDateText
import com.memeusix.ekspensify.ui.dashboard.budget.helper.getTagValues
import com.memeusix.ekspensify.ui.dashboard.budget.helper.isBudgetExceed
import com.memeusix.ekspensify.ui.dashboard.budget.helper.isClosed
import com.memeusix.ekspensify.ui.theme.Red100
import com.memeusix.ekspensify.ui.theme.Violet40
import com.memeusix.ekspensify.ui.theme.extendedColors
import com.memeusix.ekspensify.ui.theme.interFontFamily
import com.memeusix.ekspensify.utils.formatRupees
import com.memeusix.ekspensify.utils.roundedBorder

@Composable
fun BudgetItem(
    budget: BudgetResponseModel?,
    modifier: Modifier,
    onCategoryClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    val color = budget.getColor()

    Column(
        modifier = modifier
    ) {
        if (budget?.isDetailsPage == false) {
            BudgetItemTitle("#${budget.id}")
            VerticalSpace(8.dp)
        }
        BudgetSpentLimitRow(budget)
        VerticalSpace(8.dp)
        LinearProgressBar(
            progress = budget.getProgressValue(),
            color = color,
            modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(21.dp))
        )
        VerticalSpace(12.dp)
        BudgetDatePeriodRow(budget)
        VerticalSpace(12.dp)
        BudgetTagRow(
            budget,
            onAccountClick = onAccountClick,
            onCategoryClick = onCategoryClick
        )
    }
}

@Composable
fun BudgetItemTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.SemiBold,
            color = Violet40.copy(alpha = 0.6f)
        )
    )
}

@Composable
fun BudgetDatePeriodRow(
    budget: BudgetResponseModel?
) {
    val dateText = getStartEndDateText(budget?.startDate, budget?.endDate)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        DrawableStartText(
            text = budget.getOnGoingText(),
            icon = R.drawable.ic_prize_list,
            textSize = 12.sp,
            iconSize = 14.dp
        )
        Text(
            text = dateText,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp,
                color = MaterialTheme.extendedColors.iconColor
            ),
        )
    }
}

@Composable
private fun BudgetSpentLimitRow(budget: BudgetResponseModel?) {
    val color = budget.getColor()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BudgetSpentLimitText(
            spent = budget?.spent ?: 0,
            limit = budget?.limit ?: 0,
            color = color
        )
        if (budget.isBudgetExceed()) {
            Text(
                stringResource(R.string.you_ve_exceed_the_limit),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = Red100
                ),
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}

@Composable
private fun BudgetTagRow(
    budget: BudgetResponseModel?,
    onCategoryClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    val (tagText, tagColor, tagIcon) = budget.getTagValues()
    val accountCount = budget?.accounts?.size ?: 0
    val categoryCount = budget?.categories?.size ?: 0

    val commonTextStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp, color = MaterialTheme.extendedColors.iconColor
    )
    val commonModifier = Modifier
        .roundedBorder(6.dp)
        .padding(5.dp)

    val textModifier = Modifier
        .fillMaxHeight()
        .roundedBorder(6.dp)
        .clip(RoundedCornerShape(6.dp))


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DrawableStartText(
            text = budget.getPeriod(),
            icon = R.drawable.ic_clock,
            modifier = commonModifier
        )
        DrawableStartText(
            text = tagText,
            icon = tagIcon,
            color = tagColor,
            modifier = commonModifier
        )
        if (budget?.isDetailsPage == true) {
            Text(
                (if (accountCount == 0) "All" else "$accountCount") + " Accounts",
                style = commonTextStyle,
                modifier = textModifier
                    .clickable(
                        enabled = accountCount != 0,
                        onClick = onAccountClick
                    )
                    .padding(horizontal = 5.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
            )
            Text(
                (if (categoryCount == 0) "All" else "$categoryCount") + " Categories",
                style = commonTextStyle,
                modifier = textModifier
                    .clickable(
                        enabled = categoryCount != 0,
                        onClick = onCategoryClick
                    )
                    .padding(horizontal = 5.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun BudgetSpentLimitText(
    spent: Int,
    limit: Int,
    color: Color,
    spentTextSize: TextUnit = 22.sp,
    limitTextSize: TextUnit = 16.sp
) {
    Text(
        text = buildAnnotatedString {
            append(spent.formatRupees())
            append(" / ")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = limitTextSize,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(limit.formatRupees())
            }
        },
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = spentTextSize, color = color
        ),
    )
}


