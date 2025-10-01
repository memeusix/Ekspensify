package com.honeypot.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.commandiron.wheel_picker_compose.core.WheelTextPicker
import com.honeypot.app.R
import com.honeypot.app.utils.SetWindowDim
import java.time.LocalDate

@Composable
fun MonthPicker(
    initialMonth: LocalDate? = LocalDate.now(),
    onDateSelected: (Pair<LocalDate, LocalDate>) -> Unit,
    onDismiss: () -> Unit
) {
    val years = MonthPickerHelper.yearsList


    val initialMonthIndex = initialMonth?.monthValue?.minus(1) ?: 0
    val initialYearIndex = years.indexOf(initialMonth?.year.toString()).takeIf { it >= 0 } ?: 0

    var selectedMonthIndex by remember { mutableIntStateOf(initialMonthIndex) }
    var selectedYearIndex by remember { mutableIntStateOf(initialYearIndex) }


    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        SetWindowDim(0.5F)
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp / 3),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    WheelTextPicker(
                        texts = MonthPickerHelper.getAvailableMonths(selectedYearIndex),
                        rowCount = 3,
                        startIndex = initialMonthIndex,
                        style = MaterialTheme.typography.bodyMedium,
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            enabled = false
                        ),
                        onScrollFinished = {
                            selectedMonthIndex = it
                            it
                        }
                    )
                    VerticalDivider(modifier = Modifier.height(30.dp))
                    WheelTextPicker(
                        texts = years,
                        rowCount = 3,
                        startIndex = initialYearIndex,
                        style = MaterialTheme.typography.bodyMedium,
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            enabled = false
                        ),
                        onScrollFinished = {
                            selectedYearIndex = it
                            it
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledButton(
                    text = stringResource(R.string.cancel),
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = onDismiss
                )
                FilledButton(
                    text = stringResource(R.string.set_month),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val selectedDate = MonthPickerHelper.getStartEndDatePair(
                            selectedMonthIndex,
                            selectedYearIndex
                        )
                        onDateSelected(selectedDate)
                    }
                )
            }
        }
    }
}

object MonthPickerHelper {
    val monthList = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    val yearsList = (1975..LocalDate.now().year).map { it.toString() }

    fun getStartEndDatePair(
        selectedMonthIndex: Int,
        selectedYearIndex: Int
    ): Pair<LocalDate, LocalDate> {
        val selectedMonth = selectedMonthIndex + 1
        val selectedYear = yearsList[selectedYearIndex].toInt()
        val startDate = LocalDate.of(selectedYear, selectedMonth, 1)
        val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())
        return Pair(startDate, endDate)
    }


    private val currentYear = LocalDate.now().year
    private val currentMonth = LocalDate.now().monthValue

    // Restrict available months based on the selected year
    fun getAvailableMonths(selectedYearIndex: Int): List<String> {
        val selectedYear = yearsList[selectedYearIndex].toInt()
        return if (selectedYear == currentYear) {
            monthList.take(currentMonth)
        } else {
            monthList
        }
    }
}