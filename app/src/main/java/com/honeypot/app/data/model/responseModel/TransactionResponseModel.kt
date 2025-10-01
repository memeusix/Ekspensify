package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class TransactionResponseModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("account")
    @Expose
    var account: AccountResponseModel? = null,

    @SerializedName("category")
    @Expose
    var category: CategoryResponseModel? = null,

    @SerializedName("attachment")
    @Expose
    var attachment: String? = null,

    @SerializedName("amount")
    @Expose
    var amount: BigDecimal? = null,

    @SerializedName("note")
    @Expose
    var note: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: String? = null,

    val pendingId: Int? = null
) : Serializable