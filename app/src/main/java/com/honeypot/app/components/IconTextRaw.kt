package com.honeypot.app.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun IconTextRaw(
    leading: @Composable RowScope.() -> Unit,
    trailing: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    iconSpacing: Dp = 8.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(iconSpacing, Alignment.CenterHorizontally)
    ) {
        leading()
        trailing()
    }
}

@Composable
fun DrawableStartText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.extendedColors.iconColor,
    textSize: TextUnit = 10.sp,
    iconSize: Dp = 12.dp,
    iconSpace: Dp = 2.dp,
    fontWeight: FontWeight = FontWeight.Normal,
    colorFilter: ColorFilter? = ColorFilter.tint(color),
    @DrawableRes icon: Int
) {
    IconTextRaw(
        modifier = modifier, iconSpacing = iconSpace,
        leading = {
            Image(
                painter = rememberAsyncImagePainter(icon),
                modifier = Modifier.size(iconSize),
                colorFilter = colorFilter,
                contentDescription = null,
            )
        },
        trailing = {
            Text(
                text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = textSize, color = color, fontWeight = fontWeight
                ),
            )
        })
}


@Composable
fun DrawableEndText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.extendedColors.iconColor,
    colorFilter: Boolean = true,
    textSize: TextUnit = 10.sp,
    iconSize: Dp = 12.dp,
    iconSpace: Dp = 2.dp,
    fontWeight: FontWeight = FontWeight.Normal,
    @DrawableRes icon: Int
) {
    IconTextRaw(
        modifier = modifier, iconSpacing = iconSpace,
        trailing = {
            Image(
                painter = rememberAsyncImagePainter(icon),
                modifier = Modifier.size(iconSize),
                colorFilter = if (colorFilter) ColorFilter.tint(color) else null,
                contentDescription = null,
            )
        },
        leading = {
            Text(
                text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = textSize, color = color, fontWeight = fontWeight
                ),
                textAlign = TextAlign.End,
            )
        })
}