package com.ekspensify.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryInsightsResponseModel(
    @SerializedName("category")
    @Expose
    var category: CategoryResponseModel? = null,
    @SerializedName("amount")
    @Expose
    var amount: Double? = 0.0
) : Serializable