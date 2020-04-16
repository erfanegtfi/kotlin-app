package com.e.kotlinapp.model.response.base;


sealed class ApiCallState {
    object Loading : ApiCallState()
    data class Loaded(val response: ApiBaseResponse) : ApiCallState()
    data class ResponseError(val throwable: Throwable, val message: ApiBaseResponse) : ApiCallState()
    data class UnAuthorizedError(val throwable: Throwable, val message: ApiBaseResponse) : ApiCallState()
    data class TimeOutError(val throwable: Throwable) : ApiCallState()
    data class NetworkError(val throwable: Throwable) : ApiCallState()
    data class UnknownError(val throwable: Throwable) : ApiCallState()
}
