package com.honeypot.app.data.repository

import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseRepository
import com.honeypot.app.data.model.requestModel.AuthRequestModel
import com.honeypot.app.data.model.responseModel.AuthResponseModel
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.data.services.AuthApi
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) : BaseRepository {

    suspend fun signUpWithGoogle(
        idToken: String
    ): ApiResponse<AuthResponseModel> {
        return handleResponse { authApi.signUpWithGoogle(idToken) }
    }

    suspend fun signInWithGoogle(
        idToken: String
    ): ApiResponse<AuthResponseModel> {
        return handleResponse { authApi.signInWithGoogle(idToken) }
    }

    suspend fun register(
        authRequestModel: AuthRequestModel
    ): ApiResponse<UserResponseModel> {
        return handleResponse { authApi.register(authRequestModel) }
    }

    suspend fun login(
        authRequestModel: AuthRequestModel
    ): ApiResponse<AuthResponseModel> {
        return handleResponse { authApi.login(authRequestModel) }
    }

    suspend fun sendOtp(
        authRequestModel: AuthRequestModel
    ): ApiResponse<Any> {
        return handleResponse { authApi.sendOtp(authRequestModel) }
    }

}