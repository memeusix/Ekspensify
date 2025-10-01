package com.honeypot.app.ui.dashboard.transactions.data

import android.annotation.SuppressLint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface Displayable {
    val displayName: String
}

enum class DateRange(
    override val displayName: String,
) : Displayable {
    TODAY("Today"),
    YESTERDAY("Yesterday"),
    THIS_WEEK("This Week"),
    LAST_WEEK("Last Week"),
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    THIS_YEAR("This Year"),
    LAST_YEAR("Last Year"),
    CUSTOM("Custom Range");

    companion object {
        fun getEntriesWithNoCustom(): List<DateRange> {
            return entries.filter { it != CUSTOM }
        }
    }
}

enum class AmountRange(
    override val displayName: String, val amountRange: Pair<Int?, Int?>
) : Displayable {
    LESS_THAN_100("Less than ₹100", null to 100),
    BETWEEN_100_500("₹100 - ₹500", 100 to 500),
    BETWEEN_500_1000("₹500 - ₹1000", 500 to 1000),
    BETWEEN_1000_2000("₹1000 - ₹2000", 1000 to 2000),
    MORE_THAN_2000("More than ₹2000", 2000 to null),
    CUSTOM("Custom Range", null to null);
}

enum class SortBy(
    override val displayName: String, val sortBy: String
) : Displayable {
    OLDEST_TO_NEWEST("Oldest to Newest", "created_at"),
    NEWEST_TO_OLDEST("Newest to Oldest", "-created_at"),
    AMOUNT_LOW_TO_HIGH("Amount Low to High", "amount"),
    AMOUNT_HIGH_TO_LOW("Amount High to Low", "-amount");
}

@SuppressLint("NewApi")
fun DateRange.getFormattedDateRange(): Pair<String?, String?> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val today = LocalDate.now()

    fun startOfDay(date: LocalDate) = date.atStartOfDay()
    fun endOfDay(date: LocalDate): String {
        val endOfDay = date.atTime(23, 59, 59)
        return endOfDay.format(formatter).removeSuffix("Z") + "Z"
    }

    fun range(start: LocalDate, end: LocalDate) =
        Pair(startOfDay(start).format(formatter), endOfDay(end).format(formatter))


    return when (this) {
        DateRange.TODAY -> range(today, today)

        DateRange.YESTERDAY -> range(today.minusDays(1), today.minusDays(1))

        DateRange.THIS_MONTH -> range(today.withDayOfMonth(1), today)

        DateRange.LAST_MONTH -> {
            val startOfMonth = today.minusMonths(1).withDayOfMonth(1)
            range(startOfMonth, startOfMonth.plusMonths(1).minusDays(1))
        }

        DateRange.THIS_WEEK -> range(
            today.with(DayOfWeek.MONDAY),
            today
        )

        DateRange.LAST_WEEK -> {
            val startOfLastWeek = today.with(DayOfWeek.MONDAY).minusWeeks(1)
            range(startOfLastWeek, startOfLastWeek.plusWeeks(1).minusDays(1))
        }

        DateRange.THIS_YEAR -> range(
            today.withDayOfYear(1),
            today
        )

        DateRange.LAST_YEAR -> {
            val startDateOfLastYear = today.minusYears(1).withDayOfYear(1)
            range(startDateOfLastYear, startDateOfLastYear.plusYears(1).minusDays(1))
        }

        DateRange.CUSTOM -> Pair(null, null)
    }
}