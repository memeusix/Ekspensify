package com.honeypot.app.data.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class TransactionRequestModel(
    @SerializedName("account_id")
    @Expose
    var accountId: Int? = null,

    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null,

    @SerializedName("attachment_id")
    @Expose
    var attachmentId: Int? = null,

    @SerializedName("amount")
    @Expose
    var amount: BigDecimal? = null,

    @SerializedName("note")
    @Expose
    var note: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null
) : Serializable