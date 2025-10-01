package com.honeypot.app.ui.acounts.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.honeypot.app.utils.formatRupees
import kotlin.math.max

@Composable
fun AmountText(
    balance: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    size: TextUnit = 50.sp,
    minSize : TextUnit = 10.sp,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    val formattedBalance = balance
        .takeIf { it.trim().isNotEmpty() && it != "-" && it.toBigDecimalOrNull() != null }
        ?.toBigDecimal()
        ?.formatRupees() ?: "0"


    var textSize : TextUnit by remember { mutableStateOf(size) }


//    Text(
//        text = formattedBalance,
//        style = MaterialTheme.typography.titleLarge.copy(
//            fontWeight = fontWeight,
//            color = color ?: Color.Unspecified
//        ),
//        modifier = modifier
//    )

    Text(
        text = formattedBalance,
        fontSize = textSize,
        maxLines = 1,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = fontWeight,
            color = color ?: Color.Unspecified
        ),
        overflow = TextOverflow.Ellipsis,
        onTextLayout = {textLayoutResult ->
            val currentMaxLinesIndex=  textLayoutResult.lineCount -1
            if (textLayoutResult.isLineEllipsized(currentMaxLinesIndex)){
                textSize *= 0.9
            }
        },
        modifier = modifier
    )
}

