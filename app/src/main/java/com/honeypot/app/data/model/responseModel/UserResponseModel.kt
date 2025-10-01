package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponseModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("accounts")
    @Expose
    var accounts: Int? = null,

    @SerializedName("is_verified")
    @Expose
    var isVerified: Boolean? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,
) : Serializable