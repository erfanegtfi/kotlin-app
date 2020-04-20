package com.e.kotlinapp.network.coroutine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.e.kotlinapp.MessageShowType
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.network.NoConnectivityException
import com.e.kotlinapp.network.UtilsError
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

//    https://github.com/herisulistiyanto/Simple-Retrofit-Coroutine/blob/master/app/src/main/java/com/andro/indie/school/data/source/remote/MovieRemoteDataSource.kt
// using ResponseResultWithWrapper
abstract class BaseDataSource2 {
    protected suspend fun <T : ApiBaseResponse> getResult(call: suspend () -> Response<T>): ResponseResultWithWrapper<ResponseWrapper<T>> {

        val result: ResponseResultWithWrapper<ResponseWrapper<T>> = callApi(call)

        when (result) {
            is ResponseResultWithWrapper.Success -> {

            }

            is ResponseResultWithWrapper.Error -> {

            }
        }

        return result;
    }

    protected suspend fun <T : ApiBaseResponse> callApi(call: suspend () -> Response<T>): ResponseResultWithWrapper<ResponseWrapper<T>> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (null != body) return onResponse(body)
            }
            return error(response.errorBody());
        } catch (e: Throwable) {
            return error(e)
        }
    }

    private fun <T> error(throwable: Throwable): ResponseResultWithWrapper<ResponseWrapper<T>> {
        val apiCallResult: ResponseResultWithWrapper<ResponseWrapper<T>>
        if (throwable is HttpException) {
            val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())

            if (message.httpCode == 0) message.httpCode = throwable.code();


            if (throwable.code() == 403 || throwable.code() == 401)
                apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(responseError= message, throwable= ResponseResultErrors.UnAuthorizedError(throwable)))
            else if (throwable.code() == 404 || throwable.code() == 500)
                apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(responseError= message))
            else {
                apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(responseError= message))
//                apiCallResult = ResponseResult.ResponseError(message)
            }

        } else if (throwable is SocketTimeoutException) {
            apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(throwable= ResponseResultErrors.TimeOutError(throwable)))
//            apiCallResult = ResponseResult.TimeOutError(throwable)
        } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
            apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(throwable= ResponseResultErrors.NetworkError(throwable)))
//            apiCallResult = ResponseResult.NetworkError(throwable)
        } else {
            Log.e("errorrrrrr ", throwable.message);
            apiCallResult = ResponseResultWithWrapper.Error(ResponseWrapper(throwable= ResponseResultErrors.UnknownError(throwable)))
//            apiCallResult = ResponseResult.UnknownError(throwable)
        }

        return apiCallResult
    }

    private fun <T> error(responseBody: ResponseBody?): ResponseResultWithWrapper<ResponseWrapper<T>> {
        val apiCallResult: ResponseResultWithWrapper<ResponseWrapper<T>>
        val response: ApiBaseResponse = UtilsError.parseError(responseBody)

        apiCallResult = ResponseResultWithWrapper.ErrorResponse(ResponseWrapper(responseError= response))

        return apiCallResult
    }


    fun <T : ApiBaseResponse> onResponse(response: T): ResponseResultWithWrapper<ResponseWrapper<T>> {
        Log.e("successfullCall: ", response.httpCode.toString());
        val apiCallResult: ResponseResultWithWrapper<ResponseWrapper<T>>
        if (response.success == true) {

            response.showType = MessageShowType.TOAST
            apiCallResult = ResponseResultWithWrapper.Success(ResponseWrapper(data=response)) //ApiCallEvent(callState= ApiCallState.LOADED,message=response)

        } else {
            apiCallResult = ResponseResultWithWrapper.ErrorResponse(ResponseWrapper( responseError=response))//ApiCallEvent(callState= ApiCallState.LOADED,message=response)
        }


        return apiCallResult
    }

}