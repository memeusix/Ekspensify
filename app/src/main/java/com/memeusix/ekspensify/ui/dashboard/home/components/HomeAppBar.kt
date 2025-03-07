package com.memeusix.ekspensify.ui.dashboard.home.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memeusix.ekspensify.R
import com.memeusix.ekspensify.components.DrawableEndText
import com.memeusix.ekspensify.components.VerticalSpace
import com.memeusix.ekspensify.data.model.responseModel.UserResponseModel
import com.memeusix.ekspensify.ui.dashboard.transactions.data.DateRange
import com.memeusix.ekspensify.ui.theme.extendedColors
import com.memeusix.ekspensify.ui.theme.goodlyFontFontFamily
import com.memeusix.ekspensify.utils.getGreetingMessage
import com.memeusix.ekspensify.utils.roundedBorder


@Composable
fun HomeAppBar(
    user: UserResponseModel?,
    selectedDateRange: DateRange,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                getGreetingMessage(), style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = goodlyFontFontFamily,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            VerticalSpace(2.dp)
            Text(
                user?.name ?: "", style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
        DrawableEndText(
            text = selectedDateRange.displayName,
            icon = R.drawable.ic_arrow_down,
            iconSize = 22.dp,
            textSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .fillMaxHeight()
                .roundedBorder(40.dp)
                .clip(RoundedCornerShape(40.dp))
                .clickable(onClick = onClick)
                .padding(start = 16.dp, end = 8.dp)
        )
    }
}

