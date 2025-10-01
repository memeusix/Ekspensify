package com.honeypot.app.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.requestModel.BudgetQueryModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import com.honeypot.app.data.services.BudgetApi

class BudgetTransactionPagingSource(
    private val budgetId: Int,
    private val reportId: Int,
    private val budgetQueryModel: BudgetQueryModel,
    private val budgetApi: BudgetApi
) : PagingSource<Int, TransactionResponseModel>(), BaseRepository {
    override fun getRefreshKey(state: PagingState<Int, TransactionResponseModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionResponseModel> {
        val page = params.key ?: budgetQueryModel.page
        val limits = budgetQueryModel.limit
        return try {
            val response = handleResponse {
                budgetApi.getBudgetTransactions(
                    budgetId = budgetId,
                    reportId = reportId,
                    page = page,
                    limit = limits
                )
            }
            val isListEmpty = response.data?.items?.isEmpty()

            LoadResult.Page(
                data = response.data?.items ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (isListEmpty == true || isListEmpty == null) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}
