package com.honeypot.app.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ErrorResponseModel(
    @SerializedName("status") @Expose var code: Int? = null,

    @SerializedName("message") @Expose var message: String? = null,

    @SerializedName("reason") @Expose var reason: String? = null
) : Serializable