package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuthResponseModel(
    @SerializedName("token")
    @Expose
    var token: String? = null,

    @SerializedName("user")
    @Expose
    var user: UserResponseModel? = null,
) : Serializable