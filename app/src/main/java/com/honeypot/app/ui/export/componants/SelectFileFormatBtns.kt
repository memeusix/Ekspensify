package com.honeypot.app.ui.export.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.honeypot.app.utils.ExportFileFormat


@Composable
fun SelectFileFormatBtns(selectedFormat: MutableState<ExportFileFormat>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FileTypeBtn(
            text = ExportFileFormat.PDF.name,
            isSelected = selectedFormat.value == ExportFileFormat.PDF,
            onClick = {
                selectedFormat.value = ExportFileFormat.PDF
            }
        )
        FileTypeBtn(
            text = ExportFileFormat.CSV.name,
            isSelected = selectedFormat.value == ExportFileFormat.CSV,
            onClick = {
                selectedFormat.value = ExportFileFormat.CSV
            }
        )
    }
}


@Composable
fun RowScope.FileTypeBtn(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (color, width) = if (isSelected) {
        MaterialTheme.colorScheme.primary to 1.5.dp
    } else {
        MaterialTheme.colorScheme.surfaceVariant to 1.dp
    }
    val bg =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background

    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .border(
                shape = RoundedCornerShape(16.dp),
                color = color,
                width = width
            )
            .padding(vertical = 18.dp)
            .weight(1f)
    )
}
