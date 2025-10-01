package com.honeypot.app.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.requestModel.BudgetQueryModel
import com.honeypot.app.data.model.responseModel.BudgetReportResponseModel
import com.honeypot.app.data.services.BudgetApi

class BudgetReportPagingSource(
    private val budgetId: Int,
    private val budgetApi: BudgetApi,
    private val budgetQueryModel: BudgetQueryModel,
) : PagingSource<Int, BudgetReportResponseModel>(), BaseRepository {

    override fun getRefreshKey(state: PagingState<Int, BudgetReportResponseModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BudgetReportResponseModel> {
        val page = params.key ?: budgetQueryModel.page
        val limit = budgetQueryModel.limit

        return try {
            val response = handleResponse {
                budgetApi.getBudgetReports(
                    budgetId = budgetId,
                    limit = limit,
                    page = page
                )
            }
            val isListEnd = response.data?.items?.isEmpty()
            LoadResult.Page(
                data = response.data?.items ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (isListEnd == true || isListEnd == null) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}