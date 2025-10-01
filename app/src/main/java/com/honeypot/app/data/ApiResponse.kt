package com.honeypot.app.data

//sealed class ApiResponse<out T>(
//    val data: T?, val errorResponse: ErrorResponseModel?
//) {
//    data class Success<out T>(private val response: T?) :
//        ApiResponse<T>(data = response, errorResponse = null)
//
//    data class Failure<out T>(
//        private val error: ErrorResponseModel?,
//        private val currentData: T? = null
//    ) :
//        ApiResponse<T>(data = currentData, errorResponse = error)
//
//    data class Loading<out T>(
//        private val currentData: T? = null,
//        private val currentError: ErrorResponseModel? = null
//    ) : ApiResponse<T>(data = currentData, errorResponse = currentError)
//
//    data object Idle : ApiResponse<Nothing>(data = null, errorResponse = null)
//}

sealed class ApiResponse<out T> {

    // The current data
    abstract val data: T?

    // success state
    data class Success<out T>(override val data: T?) : ApiResponse<T>()

    // loading state
    data class Loading<out T>(override val data: T? = null) : ApiResponse<T>()

    // Failure State
    data class Failure<out T>(
        override val data: T? = null,
        val errorResponse: ErrorResponseModel?
    ) : ApiResponse<T>()

    data object Idle : ApiResponse<Nothing>() {
        override val data = null
    }

    companion object {

        fun <T> idle(): ApiResponse<T> = Idle

        fun <T> loading(data: T? = null): ApiResponse<T> = Loading(data)

        fun <T> success(data: T?): ApiResponse<T> = Success(data)

        fun <T> failure(error: ErrorResponseModel?, data: T? = null): ApiResponse<T> =
            Failure(data, error)

    }
}