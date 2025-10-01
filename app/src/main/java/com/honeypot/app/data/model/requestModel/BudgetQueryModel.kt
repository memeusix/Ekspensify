package com.honeypot.app.data.model.requestModel

import java.io.Serializable

data class BudgetQueryModel(
    val limit: Int = 10,
    var page: Int = 1,
    var refresh: Unit = Unit
) : Serializable