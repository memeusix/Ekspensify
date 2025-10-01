package com.honeypot.app.data.model.requestModel

import com.honeypot.app.ui.dashboard.transactions.data.DateRange
import com.honeypot.app.utils.CategoryType

data class InsightsQueryModel(
    val type: CategoryType = CategoryType.DEBIT,
    val dateRange: DateRange = DateRange.THIS_WEEK
)