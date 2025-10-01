package com.honeypot.app.data.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttachmentResponseModel(
    @SerializedName("attachment_id")
    @Expose
    val attachmentId: Int? = null,

    @SerializedName("path")
    @Expose
    val path: String? = null
) : Serializable