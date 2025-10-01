package com.honeypot.app.data.services

import com.honeypot.app.data.model.responseModel.UserResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {
    @GET("users/me")
    suspend fun getMe(): Response<UserResponseModel>

    @PATCH("users/me")
    suspend fun updateMe(
        @Body userResponseModel: UserResponseModel
    ): Response<UserResponseModel>
}