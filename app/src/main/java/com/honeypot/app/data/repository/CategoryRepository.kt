package com.honeypot.app.data.repository

import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.requestModel.InsightsQueryModel
import com.honeypot.app.data.model.responseModel.CategoryInsightsResponseModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.data.model.responseModel.CustomIconModel
import com.honeypot.app.data.services.CategoryApi
import com.honeypot.app.ui.dashboard.transactions.data.getFormattedDateRange
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryApi: CategoryApi
) : BaseRepository {

    suspend fun getCategories(): ApiResponse<List<CategoryResponseModel>> {
        return handleResponse { categoryApi.getCategories() }
    }

    suspend fun getCustomIcons(): ApiResponse<List<CustomIconModel>> {
        return handleResponse { categoryApi.getCustomIcons() }
    }

    suspend fun createCategory(
        categoryResponseModel: CategoryResponseModel
    ): ApiResponse<CategoryResponseModel> {
        return handleResponse { categoryApi.createCategory(categoryResponseModel) }
    }

    suspend fun deleteCategory(
        categoryId: Int
    ): ApiResponse<CategoryResponseModel> {
        return handleResponse { categoryApi.deleteCategory(categoryId) }
    }

    suspend fun getCategoryInsights(
        insightsQueryModel: InsightsQueryModel
    ): ApiResponse<List<CategoryInsightsResponseModel>> {
        return handleResponse {
            val (startDate, endDate) = insightsQueryModel.dateRange.getFormattedDateRange()
            categoryApi.getCategoryInsights(
                type = insightsQueryModel.type.name,
                startDate = startDate,
                endDate = endDate
            )
        }
    }
}