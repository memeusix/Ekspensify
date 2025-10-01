package com.honeypot.app.ui.dashboard.home.components


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
import com.honeypot.app.R
import com.honeypot.app.components.DrawableEndText
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.ui.dashboard.transactions.data.DateRange
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.ui.theme.goodlyFontFontFamily
import com.honeypot.app.utils.getGreetingMessage
import com.honeypot.app.utils.roundedBorder


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

