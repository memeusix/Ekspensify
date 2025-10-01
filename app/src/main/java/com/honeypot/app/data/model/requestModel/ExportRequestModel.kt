package com.honeypot.app.data.model.requestModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExportRequestModel(
    @SerializedName("start")
    val start: String? = null,

    @SerializedName("end")
    val end: String? = null,

    @SerializedName("format")
    val format: String? = null,
) : Serializable
