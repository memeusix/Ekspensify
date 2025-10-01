package com.honeypot.app.ui.dashboard.home.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.honeypot.app.components.HorizontalSpace
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.ui.theme.Dark_Muted_Purple
import com.honeypot.app.ui.theme.Green100
import com.honeypot.app.ui.theme.Light100
import com.honeypot.app.ui.theme.goodlyFontFontFamily
import com.honeypot.app.utils.roundedBorder

@Composable
fun AutoTrackingCard(
    isEnable: Boolean,
    onEnableClick: () -> Unit,
    onPendingTnxClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
        ,
        contentColor = Light100,
        color = Dark_Muted_Purple
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                TitleAndDescView()
                HorizontalSpace(10.dp)
                IconView()
            }
            VerticalSpace(10.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!isEnable) {
                    EnableNowBtn(onEnableClick)
                } else {
                    CurrentlyActiveText()
                }
                DrawableEndText(
                    text = stringResource(R.string.pending_transaction),
                    icon = R.drawable.ic_arrow_right_long,
                    fontWeight = FontWeight.Medium,
                    iconSize = 16.dp,
                    textSize = 12.sp,
                    color = Light100,
                    modifier = Modifier.clickable(
                        onClick = onPendingTnxClick
                    )
                )
            }
        }
    }
}

@Composable
private fun EnableNowBtn(onEnableClick: () -> Unit) {
    Text(
        stringResource(R.string.enable_now),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .roundedBorder(40.dp, Light100)
            .clip(RoundedCornerShape(40.dp))
            .clickable(onClick = onEnableClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun CurrentlyActiveText() {
    Text(
        stringResource(R.string.currently_active),
        style = MaterialTheme.typography.labelSmall.copy(
            color = Green100
        ),
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background,
                CircleShape
            )
            .padding(vertical = 6.dp, horizontal = 12.dp)
    )
}

@Composable
private fun RowScope.TitleAndDescView() {
    Column(
        modifier = Modifier.Companion.weight(1f),
    ) {
        Text(
            text = stringResource(R.string.auto_tracking),
            fontFamily = goodlyFontFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
        VerticalSpace(5.dp)
        Text(
            stringResource(R.string.track_your_expenses_automatically_with_sms_notifications_secure_private_and_effortless),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun IconView() {
    Image(
        rememberAsyncImagePainter(R.drawable.ic_hand_holding_home),
        contentDescription = null,
        modifier = Modifier.size(90.dp)
    )
}