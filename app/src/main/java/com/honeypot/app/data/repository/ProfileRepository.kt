package com.honeypot.app.data.repository

import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.data.services.ProfileApi
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi
) : BaseRepository {

    suspend fun getMe(): ApiResponse<UserResponseModel> {
        return handleResponse { profileApi.getMe() }
    }

    suspend fun updateMe(
        userResponseModel: UserResponseModel
    ): ApiResponse<UserResponseModel> {
        return handleResponse { profileApi.updateMe(userResponseModel) }
    }
}