package com.honeypot.app.data.services

import com.honeypot.app.data.model.responseModel.CategoryInsightsResponseModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.data.model.responseModel.CustomIconModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApi {

    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryResponseModel>>

    @GET("categories/custom-icons")
    suspend fun getCustomIcons(): Response<List<CustomIconModel>>

    @POST("categories")
    suspend fun createCategory(
        @Body categoryResponseModel: CategoryResponseModel
    ): Response<CategoryResponseModel>


    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: Int
    ): Response<CategoryResponseModel>

    @GET("categories/insights")
    suspend fun getCategoryInsights(
        @Query("type") type: String,
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?
    ): Response<List<CategoryInsightsResponseModel>>
}