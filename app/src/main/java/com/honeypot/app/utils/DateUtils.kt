package com.honeypot.app.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

fun String?.formatDateTime(
    format: String = DateFormat.dd_MMM_yyyy_hh_mm_a,
    inputFormat: String = DateFormat.INPUT_FORMAT
): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val istZoneId = ZoneId.of("Asia/Kolkata")
            ZonedDateTime.parse(this)
                .withZoneSameInstant(istZoneId)
                .format(DateTimeFormatter.ofPattern(format))
        } else {
            val utcFormat = SimpleDateFormat(inputFormat)
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = utcFormat.parse(this)
            val istFormat = SimpleDateFormat(format)
            istFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            istFormat.format(date)
        }
    } catch (e: Exception) {
        ""
    }
}

fun String?.formatLocalDate(format: String = DateFormat.MMM_dd_yyyy): String? =
    if (this.isNullOrBlank()) null
    else LocalDate.parse(this)?.format(DateTimeFormatter.ofPattern(format))

fun LocalDate?.formatLocalDate(format: String = DateFormat.MMM_dd_yyyy): String? =
    this?.format(DateTimeFormatter.ofPattern(format))

fun String?.formatLocalDateTime(pattern: String = DateFormat.MMM_dd_yyyy_hh_mm_a): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)

    // Parse the string into a LocalDateTime object
    val dateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    // Return the formatted date-time string
    return dateTime.format(formatter)
}

fun String?.formatZonedDateTime(pattern: String =DateFormat.MMM_dd_yyyy_hh_mm_a): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)

    // Parse the string into a ZonedDateTime object
    val dateTime = ZonedDateTime.parse(this, DateTimeFormatter.ISO_ZONED_DATE_TIME)

    // Format the ZonedDateTime as per the given pattern
    return dateTime.format(formatter)
}


object DateFormat {
    const val INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val dd_MMM_yyyy_hh_mm_a = "dd-MMM-yyyy hh:mm a"
    const val dd_MMM_yyyy = "dd-MMM-yyyy"
    const val yyyy_mm_dd = "yyyy-MM-dd"

    //MMM dd, yyyy
    const val MMM_dd_yyyy = "MMM dd, yyyy"
    const val MMM_dd_yyyy_hh_mm_a = "MMM dd, yyyy hh:mm a"
    const val dd_MMM_yy = "dd MMM, yy"
    const val dd_MMM_yyyy_ = "dd MMM, yyyy"
}

fun getGreetingMessage(): String {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (currentHour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}