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
    data class UnAuthorizedError<T>(val throwable: Throwable) : ResponseResult<T>()
    data class TimeOutError<T>(val throwable: Throwable) : ResponseResult<T>()
    data class NetworkError<T>(val throwable: Throwable) : ResponseResult<T>()
    data class UnknownError<T>(val throwable: Throwable) : ResponseResult<T>()

    companion object {


    }

}

//data class ResponseWrapper<out T>(val data: T?, val errorMsg: String?)