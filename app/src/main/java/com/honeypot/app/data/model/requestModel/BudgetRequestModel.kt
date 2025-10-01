package com.honeypot.app.data.model.requestModel

import com.google.gson.annotations.SerializedName
import com.honeypot.app.utils.BudgetPeriod
import com.honeypot.app.utils.BudgetType
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class BudgetRequestModel(
    @SerializedName("limit")
    val limit: BigDecimal = BigDecimal.ZERO,

    @SerializedName("account_ids")
    val accountIds: List<Int>? = null,

    @SerializedName("category_ids")
    val categoryIds: List<Int>? = null,

    @SerializedName("period")
    val period: String = BudgetPeriod.MONTHLY.name,

    @SerializedName("type")
    val type: String = BudgetType.RECURRING.name,

    @SerializedName("start_date")
    val startDate: String = LocalDate.now().toString(),

    @SerializedName("end_date")
    val endDate: String? = null
) : Serializable
