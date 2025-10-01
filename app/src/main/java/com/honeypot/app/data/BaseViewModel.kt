package com.honeypot.app.data

interface BaseViewModel {

    /**
     * Handles API response state transitions based on the result of the API call.
     *
     * @param currentData The current state of the data.
     * @param call A suspend function that makes the API call and returns an ApiResponse.
     * @return The updated ApiResponse based on the API result.
     */
    suspend fun <T> handleData(
        currentData: ApiResponse<T>,
        call: suspend () -> ApiResponse<T>
    ): ApiResponse<T> {
        return when (val response = call()) {

            is ApiResponse.Success -> response
            is ApiResponse.Failure -> ApiResponse.failure(
                error = response.errorResponse,
                data = currentData.data
            )

            else -> currentData
        }
    }


}