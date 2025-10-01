package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class AcSummaryResponseModel(
    @SerializedName("total")
    @Expose
    var total: BigDecimal? = null,
    @SerializedName("credit")
    @Expose
    var credit: BigDecimal? = null,

    @SerializedName("debit")
    @Expose
    var debit: BigDecimal? = null
) : Serializable