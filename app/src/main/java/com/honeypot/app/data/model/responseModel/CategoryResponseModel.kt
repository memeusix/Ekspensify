package com.honeypot.app.data.model.responseModel

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Immutable
data class CategoryResponseModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("slug")
    @Expose
    var slug: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("icon")
    @Expose
    var icon: String? = null,

    @SerializedName("ic_fill_color")
    @Expose
    val icFillColor: String? = null,

    @SerializedName("icon_id")
    @Expose
    var iconId: Int? = null,

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: String? = null
) : Serializable

data class CategoryListModel(
    val categories: List<CategoryResponseModel>
) : Serializable