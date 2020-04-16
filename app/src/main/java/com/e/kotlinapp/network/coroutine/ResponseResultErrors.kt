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

sealed class ResponseResultErrors {

    data class UnAuthorizedError(val throwable: Throwable) : ResponseResultErrors()
    data class TimeOutError(val throwable: Throwable) : ResponseResultErrors()
    data class NetworkError(val throwable: Throwable) : ResponseResultErrors()
    data class UnknownError(val throwable: Throwable) : ResponseResultErrors()

    companion object {

    }

}
