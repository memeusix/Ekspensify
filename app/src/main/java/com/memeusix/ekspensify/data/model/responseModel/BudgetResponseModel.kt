package com.memeusix.ekspensify.data.model.responseModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BudgetResponseModel(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("limit")
    var limit: Int? = null,

    @SerializedName("spent")
    var spent: Int? = null,

    @SerializedName("accounts")
    var accounts: List<AccountResponseModel>? = null,

    @SerializedName("categories")
    var categories: List<CategoryResponseModel>? = null,

    @SerializedName("period")
    var period: String? = null,

    @SerializedName("period_no")
    var periodNo: Int? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("start_date")
    var startDate: String? = null,

    @SerializedName("end_date")
    var endDate: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null,

    val isDetailsPage : Boolean = false
) : Serializable