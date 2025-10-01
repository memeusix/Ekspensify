package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class CategoryInsightsResponseModel(
    @SerializedName("category")
    @Expose
    var category: CategoryResponseModel? = null,
    @SerializedName("amount")
    @Expose
    var amount: BigDecimal? = BigDecimal.ZERO
) : Serializable