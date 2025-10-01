package com.honeypot.app.data.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class AccountRequestModel(
    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("balance")
    @Expose
    val balance: BigDecimal? = null,
) : Serializable
