package com.honeypot.app.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.requestModel.TransactionQueryModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import com.honeypot.app.data.services.TransactionApi
import okio.IOException

class TransactionPagingSource(
    private val transactionApi: TransactionApi,
    private val transactionQueryModel: TransactionQueryModel
) : PagingSource<Int, TransactionResponseModel>(), BaseRepository {

    override fun getRefreshKey(state: PagingState<Int, TransactionResponseModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionResponseModel> {
        val page = params.key ?: transactionQueryModel.page
        val limit = transactionQueryModel.limit

        return try {
            val response = handleResponse {
                transactionApi.getTransactions(
                    limit = limit,
                    page = page,
                    type = transactionQueryModel.type,
                    accountIds = transactionQueryModel.accountIds?.joinToString(","),
                    categoryIds = transactionQueryModel.categoryIds?.joinToString(","),
                    minAmount = transactionQueryModel.minAmount,
                    maxAmount = transactionQueryModel.maxAmount,
                    startDate = transactionQueryModel.startDate,
                    endDate = transactionQueryModel.endDate,
                    sort = transactionQueryModel.sort,
                    q = transactionQueryModel.q
                )
            }
            val isListEnd = response.data?.items?.isEmpty()
            LoadResult.Page(
                data = response.data?.items ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (isListEnd == true || isListEnd == null) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}