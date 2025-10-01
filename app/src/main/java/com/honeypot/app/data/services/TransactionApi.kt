package com.honeypot.app.data.services

import com.honeypot.app.data.model.PaginationModel
import com.honeypot.app.data.model.requestModel.TransactionRequestModel
import com.honeypot.app.data.model.responseModel.AttachmentResponseModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApi {

    @POST("transactions")
    suspend fun createTransaction(
        @Body transactionRequestModel: TransactionRequestModel
    ): Response<TransactionResponseModel>

    //user can update note and attachment
    @PATCH("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body transactionRequestModel: TransactionRequestModel
    ): Response<TransactionResponseModel>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int
    ): Response<TransactionResponseModel>

    @Multipart
    @POST("transactions/upload-attachment")
    suspend fun uploadAttachment(
        @Part attachment: MultipartBody.Part
    ): Response<AttachmentResponseModel>


    @GET("transactions")
    suspend fun getTransactions(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("type") type: String? = null,
        @Query("account_ids") accountIds: String? = null,
        @Query("category_ids") categoryIds: String? = null,
        @Query("amount[gte]") minAmount: Int? = null,
        @Query("amount[lte]") maxAmount: Int? = null,
        @Query("created_at[gte]") startDate: String? = null,
        @Query("created_at[lte]") endDate: String? = null,
        @Query("sort") sort: String? = null,
        @Query("q") q: String? = null,
    ): Response<PaginationModel<TransactionResponseModel>>
}