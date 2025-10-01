package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomIconModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("is_active")
    @Expose
    var name: Boolean? = null,

    @SerializedName("icon_id")
    @Expose
    var iconId: Int? = null,

    @SerializedName("path")
    @Expose
    var path: String? = null,

    @SerializedName("ic_fill_color")
    @Expose
    val icFillColor: String? = null

) : Serializable