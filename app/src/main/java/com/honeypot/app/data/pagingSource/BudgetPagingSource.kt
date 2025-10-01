package com.honeypot.app.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.BudgetMeta
import com.honeypot.app.data.model.requestModel.BudgetQueryModel
import com.honeypot.app.data.model.responseModel.BudgetResponseModel
import com.honeypot.app.data.services.BudgetApi

class BudgetPagingSource(
    private val budgetApi: BudgetApi,
    private val budgetQueryModel: BudgetQueryModel,
    private val updateMetaDate: (BudgetMeta?) -> Unit
) : PagingSource<Int, BudgetResponseModel>(), BaseRepository {

    override fun getRefreshKey(state: PagingState<Int, BudgetResponseModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BudgetResponseModel> {
        val page = params.key ?: budgetQueryModel.page
        val limit = budgetQueryModel.limit


        return try {
            val response = handleResponse {
                budgetApi.getBudgets(
                    limit = limit, page = page
                )
            }

            val metaData = response.data?.meta
            val isListEnd = response.data?.items?.isEmpty()

            updateMetaDate(metaData)

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