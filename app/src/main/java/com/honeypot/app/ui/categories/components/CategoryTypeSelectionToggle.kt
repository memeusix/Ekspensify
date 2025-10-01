package com.honeypot.app.ui.categories.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.ui.acounts.components.CustomToggleButton
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.CategoryType

@Composable
fun CategoryTypeSelectionToggle(
    selectedCategoryType: CategoryType,
    onChange: (CategoryType) -> Unit
) {
    val categoryTypeEntries by remember { mutableStateOf(CategoryType.entries.toList()) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categoryTypeEntries.forEach { categoryType ->
            CustomToggleButton(
                text = categoryType.displayName,
                isSelected = selectedCategoryType == categoryType,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .border(
                        1.dp,
                        if (selectedCategoryType == categoryType) MaterialTheme.colorScheme.secondary else MaterialTheme.extendedColors.primaryBorder,
                        RoundedCornerShape(24.dp)
                    )
                    .padding(vertical = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onChange(categoryType) }
                    ),
                fontStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
            )
        }
    }
}