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

abstract class BaseDataSource {
    // safeApiCall
    protected suspend fun <T : ApiBaseResponse> getResult(call: suspend () -> Response<T>): ResponseResult<T> {
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

    private fun <T> error(throwable: Throwable): ResponseResult<T> {
        val apiCallResult: ResponseResult<T>
        if (throwable is HttpException) {
            val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())

            if (message.httpCode == 0) message.httpCode = throwable.code();


            if (throwable.code() == 403 || throwable.code() == 401) apiCallResult = ResponseResult.UnAuthorizedError(throwable)
            else if (throwable.code() == 404 || throwable.code() == 500) apiCallResult = ResponseResult.UnknownError(throwable)
            else {
                apiCallResult = ResponseResult.ResponseError(message)
            }

        } else if (throwable is SocketTimeoutException) {
            apiCallResult = ResponseResult.TimeOutError(throwable)
        } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
            apiCallResult = ResponseResult.NetworkError(throwable)
        } else {
            Log.e("errorrrrrr ", throwable.message);
            apiCallResult = ResponseResult.UnknownError(throwable)
        }

        return apiCallResult
    }

    private fun <T> error(responseBody: ResponseBody?): ResponseResult<T> {
        val apiCallResult: ResponseResult<T>
        val message: ApiBaseResponse = UtilsError.parseError(responseBody)

        apiCallResult = ResponseResult.ResponseError(message)

        return apiCallResult
    }


    fun <T : ApiBaseResponse> onResponse(response: T): ResponseResult<T> {
        Log.e("successfullCall: ", response.httpCode.toString());
        val apiCallResult: ResponseResult<T>
        if (response.success == true) {

            response.showType = MessageShowType.TOAST
            apiCallResult = ResponseResult.Success(response) //ApiCallEvent(callState= ApiCallState.LOADED,message=response)

        } else {
            apiCallResult = ResponseResult.ResponseError(response)//ApiCallEvent(callState= ApiCallState.LOADED,message=response)
        }


        return apiCallResult
    }

}

fun <T> resultLiveData(call: suspend () -> ResponseResult<T>): LiveData<ResponseResult<T>> {
    return liveData(Dispatchers.IO) {
//        emit(ResponseResult.Loading)
//        emit(call.invoke())
    }
}


