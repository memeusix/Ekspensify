package com.memeusix.ekspensify.data.repository

import com.memeusix.ekspensify.data.ApiResponse
import com.memeusix.ekspensify.data.BaseRepository
import com.memeusix.ekspensify.data.model.requestModel.AccountRequestModel
import com.memeusix.ekspensify.data.model.requestModel.InsightsQueryModel
import com.memeusix.ekspensify.data.model.responseModel.AcSummaryResponseModel
import com.memeusix.ekspensify.data.model.responseModel.AccountResponseModel
import com.memeusix.ekspensify.data.services.AccountApi
import com.memeusix.ekspensify.ui.dashboard.transactions.data.getFormattedDateRange
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountApi: AccountApi
) : BaseRepository {

    suspend fun createAccount(
        accountRequestModel: AccountRequestModel
    ): ApiResponse<AccountResponseModel> {
        return handleResponse { accountApi.createAccount(accountRequestModel) }
    }


    suspend fun updateAccount(
        accountId: Int,
        accountRequestModel: AccountRequestModel
    ): ApiResponse<AccountResponseModel> {
        return handleResponse { accountApi.updateAccount(accountId, accountRequestModel) }
    }


    suspend fun deleteAccount(
        accountId: Int
    ): ApiResponse<AccountResponseModel> {
        return handleResponse { accountApi.deleteAccount(accountId) }
    }


    suspend fun getAllAccounts(): ApiResponse<List<AccountResponseModel>> {
        return handleResponse { accountApi.getAllAccounts() }
    }

    suspend fun getSummary(insightsQueryModel: InsightsQueryModel): ApiResponse<AcSummaryResponseModel> {
        val (startDate, endDate) = insightsQueryModel.dateRange.getFormattedDateRange()
        return handleResponse {
            accountApi.getSummary(
                startDate = startDate,
                endDate = endDate
            )
        }
    }
}