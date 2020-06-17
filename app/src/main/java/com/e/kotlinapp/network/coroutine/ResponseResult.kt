package com.e.kotlinapp.network.coroutine

import android.util.Log
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.model.response.base.ApiCallState
import com.e.kotlinapp.network.NoConnectivityException
import com.e.kotlinapp.network.UtilsError
import com.e.kotlinapp.network.flow.ApiEmptyResponse
import com.e.kotlinapp.network.flow.ApiErrorResponse
import com.e.kotlinapp.network.flow.ApiResponse
import com.e.kotlinapp.network.flow.ApiSuccessResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by herisulistiyanto on 01/11/19.
 * KjokenKoddinger
 */

sealed class ResponseResult<out T> {

    object Loading : ResponseResult<Nothing>()
    data class Success<T>(val response: T) : ResponseResult<T>()
    data class ResponseError<T>(val response: ApiBaseResponse) : ResponseResult<T>()
    data class UnAuthorizedError<T>(val response: ApiBaseResponse) : ResponseResult<T>()
    data class TimeOutError<T>(val throwable: Throwable) : ResponseResult<T>()
    data class NetworkError<T>(val throwable: Throwable) : ResponseResult<T>()
    data class UnknownError<T>(val throwable: Throwable) : ResponseResult<T>()

    companion object {

//        fun <T> create(response: Response<T>): ResponseResult<T> {
//            return if (response.isSuccessful) {
//                val body = response.body()
////                if (body != null && response.code() != 204) {
//                    Success(body!!)
////                }
//            } else {
//                error(response.errorBody())
//            }
//        }
//
//        fun <T> create(error: Throwable): ResponseResult<T> {
//            return error(error)
//        }
//
//        private fun <T> error(throwable: Throwable): ResponseResult<T> {
//            val apiCallResult: ResponseResult<T>
//            if (throwable is HttpException) {
//                val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())
//
//                if (message.httpCode == 0) message.httpCode = throwable.code();
//
//
//                if (throwable.code() == 403 || throwable.code() == 401) apiCallResult = ResponseResult.UnAuthorizedError(throwable)
//                else if (throwable.code() == 404 || throwable.code() == 500) apiCallResult = ResponseResult.UnknownError(throwable)
//                else {
//                    apiCallResult = ResponseResult.ResponseError(message)
//                }
//
//            } else if (throwable is SocketTimeoutException) {
//                apiCallResult = ResponseResult.TimeOutError(throwable)
//            } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
//                apiCallResult = ResponseResult.NetworkError(throwable)
//            } else {
//                Log.e("errorrrrrr ", throwable.message);
//                apiCallResult = ResponseResult.UnknownError(throwable)
//            }
//
//            return apiCallResult
//        }
//
//        private fun <T> error(responseBody: ResponseBody?): ResponseResult<T> {
//            val apiCallResult: ResponseResult<T>
//            val message: ApiBaseResponse = UtilsError.parseError(responseBody)
//
//            apiCallResult = ResponseResult.ResponseError(message)
//
//            return apiCallResult
//        }

//        fun  loading() = Loading
//        fun <T> success(response: T) = Success(response)
    }

}

//data class ResponseWrapper<out T>(val data: T?, val errorMsg: String?)