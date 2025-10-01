package com.honeypot.app.ui.dashboard.budget.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.utils.CustomCornerShape
import com.honeypot.app.utils.roundedBorder

@Composable
fun DateFieldWithIcon(
    text: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp, fontWeight = FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxHeight()
                .clip(CustomCornerShape(topLeft = 16.dp, bottomLeft = 16.dp))
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .roundedBorder(CustomCornerShape(topLeft = 16.dp, bottomLeft = 16.dp))
                .padding(horizontal = 16.dp)
                .clickable(onClick = onClick, interactionSource = null, indication = null)
                .wrapContentHeight(Alignment.CenterVertically)
        )
        Image(
            painter = rememberAsyncImagePainter(R.drawable.ic_calender),
            contentDescription = "Calendar Icon",
            modifier = Modifier
                .clip(CustomCornerShape(topRight = 16.dp, bottomRight = 16.dp))
                .background(MaterialTheme.colorScheme.background)
                .roundedBorder(CustomCornerShape(topRight = 16.dp, bottomRight = 16.dp))
                .clickable(onClick = onClick)
                .padding(vertical = 11.dp, horizontal = 16.dp)
                .size(32.dp)
        )
    }
}