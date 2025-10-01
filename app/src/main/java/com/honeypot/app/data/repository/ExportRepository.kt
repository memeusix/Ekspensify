package com.honeypot.app.data.repository

import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.requestModel.ExportRequestModel
import com.honeypot.app.data.services.ExportApi
import javax.inject.Inject

class ExportRepository @Inject constructor(
    private val exportApi: ExportApi
) : BaseRepository {
    suspend fun requestStatements(
        exportRequestModel: ExportRequestModel
    ): ApiResponse<Any> {
        return handleResponse { exportApi.requestStatements(exportRequestModel) }
    }
}