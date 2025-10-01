package com.honeypot.app.ui.dashboard.budget.helper

import androidx.compose.ui.graphics.Color
import com.honeypot.app.R
import com.honeypot.app.data.model.responseModel.BudgetReportResponseModel
import com.honeypot.app.data.model.responseModel.BudgetResponseModel
import com.honeypot.app.ui.theme.Green100
import com.honeypot.app.ui.theme.Grey70
import com.honeypot.app.ui.theme.Red100
import com.honeypot.app.ui.theme.Teal
import com.honeypot.app.ui.theme.Yellow100
import com.honeypot.app.utils.BudgetPeriod
import com.honeypot.app.utils.BudgetType
import com.honeypot.app.utils.DateFormat
import com.honeypot.app.utils.formatDateTime
import com.honeypot.app.utils.getFirstWord
import java.math.BigDecimal

fun BudgetResponseModel?.getTagValues(): Triple<String, Color, Int> {
    return when (this?.status) {
        "CLOSED" -> Triple(
            "Closed",
            Grey70,
            R.drawable.ic_close_circle
        )

        else -> when (this?.type) {
            BudgetType.EXPIRING.name -> Triple(
                BudgetType.EXPIRING.displayName.getFirstWord(),
                Yellow100,
                R.drawable.ic_expiring
            )

            BudgetType.RECURRING.name -> Triple(
                BudgetType.RECURRING.displayName.getFirstWord(),
                Teal,
                R.drawable.ic_recurring
            )

            else -> Triple("", Color.Transparent, 0)
        }
    }
}

fun BudgetResponseModel?.getOnGoingText(): String {
    return when (this?.period) {
        BudgetPeriod.DAILY.name -> "Active Day: ${this.periodNo}"
        BudgetPeriod.WEEKLY.name -> "Active Week: ${this.periodNo}"
        BudgetPeriod.MONTHLY.name -> "Active Month: ${this.periodNo}"
        else -> ""
    }
}

fun BudgetResponseModel?.getPeriod(): String {
    return when (this?.period) {
        BudgetPeriod.DAILY.name -> "Daily"
        BudgetPeriod.WEEKLY.name -> "Weekly"
        BudgetPeriod.MONTHLY.name -> "Monthly"
        else -> ""
    }
}


fun BudgetReportResponseModel?.getReportPeriod(type: String): String {
    return when (type) {
        BudgetPeriod.DAILY.name -> "Day: ${this?.periodNo}"
        BudgetPeriod.WEEKLY.name -> "Week: ${this?.periodNo}"
        BudgetPeriod.MONTHLY.name -> "Month: ${this?.periodNo}"
        else -> ""
    }
}

fun getPeriodDateFormated(startDate: String?, endDate: String?): String {
    val startDateFormat = startDate?.formatDateTime(DateFormat.dd_MMM_yy)
    val endDateFormat = endDate?.formatDateTime(DateFormat.dd_MMM_yy)
    return "$startDateFormat to $endDateFormat"
}


fun BudgetResponseModel?.isBudgetExceed(): Boolean {
    return (this?.limit ?: BigDecimal.ZERO) < (this?.spent ?: BigDecimal.ZERO)
}

fun isBudgetExceed(limit: Double?, spent: Double?): Boolean {
    return (limit ?: 0.0) < (spent ?: 0.0)
}

fun isBudgetExceed(limit: BigDecimal?, spent: BigDecimal?): Boolean {
    return (limit ?: BigDecimal.ZERO) < (spent ?: BigDecimal.ZERO)
}
fun BudgetResponseModel?.getProgressValue(): Float {
    val limit = this?.limit ?: 0
    val spent = this?.spent ?: 0

    if (limit == 0) return 0f

    return (spent.toFloat() / limit.toFloat()).coerceAtMost(1f)
}

fun getStartEndDateText(startDate: String?, endDate: String?): String {
    val startDateFormatted = startDate?.formatDateTime(DateFormat.dd_MMM_yy)
    val endDateFormatted = endDate?.formatDateTime(DateFormat.dd_MMM_yy) ?: "Present"
    return "$startDateFormatted - $endDateFormatted"
}


fun BudgetResponseModel?.getColor(): Color {
    return when {
        this?.status == "CLOSED" -> Grey70
        this.isBudgetExceed() -> Red100
        else -> Green100
    }
}

fun BudgetResponseModel?.isClosed(): Boolean {
    return this?.status == "CLOSED"
}