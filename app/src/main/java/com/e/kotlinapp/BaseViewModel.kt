package com.e.kotlinapp;

import android.app.Application;
import android.util.Log;
import androidx.databinding.ObservableField

import androidx.lifecycle.AndroidViewModel;
import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.network.NoConnectivityException
import com.e.kotlinapp.network.UtilsError
import com.e.kotlinapp.model.response.base.ApiCallState.*
import com.e.kotlinapp.network.coroutine.ResponseResult
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException


import java.net.SocketTimeoutException;


open class BaseViewModel : AndroidViewModel {

    var listLoadingState: ObservableField<ListLoadState> =  ObservableField(ListInitial)
    var apiEvents: SingleLiveEvent<ResponseResult<ApiBaseResponse>> = SingleLiveEvent()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()


    constructor (application: Application) : super(application)

    open fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onError(throwable: Throwable, showOutMessage: Boolean): ResponseResult<ApiBaseResponse> {
        val apiCallResult :ResponseResult<ApiBaseResponse>
        if (throwable is HttpException) {
            val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())

            if (message.httpCode == 0)
                message.httpCode = throwable.code();


            if (throwable.code() == 403 || throwable.code() == 401)
                apiCallResult = ResponseResult.UnAuthorizedError(message)
            else if (throwable.code() == 404 || throwable.code() == 500)
                apiCallResult = ResponseResult.ResponseError( message)
            else {
                apiCallResult = ResponseResult.ResponseError( message)
            }

        } else if (throwable is SocketTimeoutException) {
            apiCallResult = ResponseResult.TimeOutError(throwable)
        } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
            apiCallResult = ResponseResult.NetworkError(throwable)
        } else {
            Log.e("errorrrrrr ", throwable.message);
            apiCallResult = ResponseResult.UnknownError(throwable)
        }

        if (showOutMessage)
            apiEvents.value = apiCallResult;

        return  apiCallResult
    }

    fun onResponse(response: ApiBaseResponse, showOutMessage: Boolean): ResponseResult<ApiBaseResponse> {
        Log.e("successfullCall: ", response.httpCode.toString());
        val apiCallResult :ResponseResult<ApiBaseResponse>
        if (response.httpCode == Constants.CODE_SUCCESS) {
            response.success = true;

            response.showType = MessageShowType.TOAST
            apiCallResult = ResponseResult.Success(response) //ApiCallEvent(callState= ApiCallState.LOADED,message=response)

        } else {
            apiCallResult = ResponseResult.ResponseError(response)//ApiCallEvent(callState= ApiCallState.LOADED,message=response)
        }

        if (showOutMessage)
            apiEvents.value = ResponseResult.Success(response)

        return apiCallResult
    }

    fun isEmptyList(total: Int, messageStr: Int) {
        if (total == 0) {
            listLoadingState.set(ListError("empty list"))
        }
    }


}
