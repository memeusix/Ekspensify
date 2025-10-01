package com.honeypot.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.honeypot.app.ui.theme.Red80


@Composable
fun AlertDialog(
    title: String = "Are you sure",
    message: String = "",
    btnText : String = "Delete",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(0.9f),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp
                    )
                )
                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        )
                    )
                }
                VerticalSpace(4.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val buttonModifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp)

                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = buttonModifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .clickable { onDismiss() }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    )

                    Text(
                        text = btnText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = buttonModifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Red80)
                            .clickable { onConfirm() }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}
