package com.honeypot.app.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaginationModel<T>(
    @SerializedName("limit") @Expose var limit: Int? = null,

    @SerializedName("next") @Expose var next: Int? = null,

    @SerializedName("offset") @Expose var offset: Int? = null,

    @SerializedName("prev") @Expose var prev: Int? = null,

    @SerializedName("total") @Expose var total: Int? = null,

    @SerializedName("meta") @Expose var meta: BudgetMeta? = null,

    @SerializedName("items") @Expose var items: List<T> = emptyList()
) : Serializable


data class BudgetMeta(
    @SerializedName("recurring") @Expose var recurring: Int? = 0,

    @SerializedName("expiring") @Expose var expiring: Int? = 0,

    @SerializedName("closed") @Expose var closed: Int? = 0,

    @SerializedName("running") @Expose var running: Int? = 0
) : Serializable