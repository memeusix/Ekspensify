package com.honeypot.app.data.model.requestModel

import java.io.Serializable

data class TransactionQueryModel(
    val limit: Int = 10,
    var page: Int = 1,

    val type: String? = null,
    val accountIds: List<Int?>? = null,
    val categoryIds: List<Int?>? = null,
    val minAmount: Int? = null,
    val maxAmount: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val accountId: Int? = null,
    val sort: String? = null,
    val q: String? = null,

    val updateList: Boolean = false,
) : Serializable
