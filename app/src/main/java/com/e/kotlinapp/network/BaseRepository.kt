package com.e.kotlinapp.network

import android.util.Log
import com.e.kotlinapp.MessageShowType
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.network.coroutine.ResponseResultErrors
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import com.e.kotlinapp.network.coroutine.ResponseWrapper
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

open class BaseRepository {

    protected fun <T> error(throwable: Throwable): ResponseResultWithWrapper<ResponseWrapper<T>> {
        val apiCallResult: ResponseResultWithWrapper<ResponseWrapper<T>>
        if (throwable is HttpException) {
            val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())

            if (message.httpCode == 0) message.httpCode = throwable.code();


            if (throwable.code() == 403 || throwable.code() == 401) apiCallResult = ResponseResultWithWrapper.Error(
                ResponseWrapper(
                    responseError = message, throwable = ResponseResultErrors.UnAuthorizedError(throwable)
                )
            )
            else if (throwable.code() == 404 || throwable.code() == 500) apiCallResult =
                ResponseResultWithWrapper.Error(ResponseWrapper(responseError = message))
            else {
                apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(responseError = message))
//                apiCallResult = ResponseResult.ResponseError(message)
            }

        } else if (throwable is SocketTimeoutException) {
            apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(throwable = ResponseResultErrors.TimeOutError(throwable)))
        } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
            apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(throwable = ResponseResultErrors.NetworkError(throwable)))
        } else {
            Log.e("errorrrrrr ", throwable.message);
            apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(throwable = ResponseResultErrors.UnknownError(throwable)))
        }

        return apiCallResult
    }

    protected fun <T> error(responseBody: ResponseBody?): ResponseResultWithWrapper<ResponseWrapper<T>> {
        val apiCallResult: ResponseResultWithWrapper<ResponseWrapper<T>>
        val response: ApiBaseResponse = UtilsError.parseError(responseBody)

        apiCallResult = ResponseResultWithWrapper.ErrorResponse(ResponseWrapper(responseError = response))

        return apiCallResult
    }


    protected fun <T : ApiBaseResponse> onResponse(response: T): ResponseResultWithWrapper<ResponseWrapper<T>> {
        val apiCallResult: ResponseResultWithWrapper<ResponseWrapper<T>>

        Log.e("successfullCall: ", response.httpCode.toString());

        if (response.success == true) {
            response.showType = MessageShowType.TOAST
            apiCallResult = ResponseResultWithWrapper.Success(ResponseWrapper(data = response))
        } else {
            apiCallResult = ResponseResultWithWrapper.ErrorResponse(ResponseWrapper(responseError = response))
        }

        return apiCallResult
    }
}