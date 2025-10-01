package com.honeypot.app.data.services

import com.honeypot.app.data.model.PaginationModel
import com.honeypot.app.data.model.requestModel.BudgetRequestModel
import com.honeypot.app.data.model.responseModel.BudgetReportResponseModel
import com.honeypot.app.data.model.responseModel.BudgetResponseModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BudgetApi {

    @POST("budgets")
    suspend fun createBudget(
        @Body budgetRequestModel: BudgetRequestModel
    ): Response<BudgetResponseModel>

    @DELETE("budgets/{id}")
    suspend fun deleteBudget(
        @Path("id") budgetId: Int
    ): Response<BudgetResponseModel>

    @FormUrlEncoded
    @PATCH("budgets/{id}")
    suspend fun updateBudget(
        @Path("id") budgetId: Int,
        @Field("status") status: String
    ): Response<BudgetResponseModel>

    @GET("budgets/{id}")
    suspend fun getBudgetById(
        @Path("id") budgetId: Int
    ): Response<BudgetResponseModel>

    @GET("budgets")
    suspend fun getBudgets(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): Response<PaginationModel<BudgetResponseModel>>

    @GET("budgets/{id}/reports")
    suspend fun getBudgetReports(
        @Path("id") budgetId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): Response<PaginationModel<BudgetReportResponseModel>>


    @GET("budgets/{id}/reports/{report_id}/transactions")
    suspend fun getBudgetTransactions(
        @Path("id") budgetId: Int,
        @Path("report_id") reportId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): Response<PaginationModel<TransactionResponseModel>>


}