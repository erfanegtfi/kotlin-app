package com.e.kotlinapp.model.response.base;


sealed class ApiCallState {
    object Loading : ApiCallState()
    data class Loaded(val response: ApiBaseResponse?=null) : ApiCallState()
    data class ResponseError( val message: ApiBaseResponse?=null) : ApiCallState()
    data class UnAuthorizedError(val message: ApiBaseResponse?=null) : ApiCallState()
    data class TimeOutError(val message: String?=null) : ApiCallState()
    data class NetworkError(val message: String?=null) : ApiCallState()
    data class UnknownError(val message: String?=null) : ApiCallState()
}
