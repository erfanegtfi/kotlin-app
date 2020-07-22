package com.e.kotlinapp;

import android.app.Application;
import androidx.databinding.ObservableField

import androidx.lifecycle.AndroidViewModel;
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.network.BaseRepository
import com.e.kotlinapp.network.coroutine.ResponseResult
import com.e.kotlinapp.network.coroutine.ResponseResultErrors
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import com.e.kotlinapp.network.coroutine.ResponseWrapper
import io.reactivex.disposables.CompositeDisposable


open class BaseViewModel : AndroidViewModel {

    var listLoadingState: ObservableField<ListLoadState> =  ObservableField(ListInitial)
    var apiEvents: SingleLiveEvent<ApiCallState> = SingleLiveEvent()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()


    constructor (application: Application) : super(application)

    open fun onDestroy() {
        compositeDisposable.clear()
    }


    private fun errorMapper(response: ResponseResultErrors?) {

        apiEvents.value =
            when (response) {
                is ResponseResultErrors.NetworkError -> ApiCallState.NetworkError(response.message)
                is ResponseResultErrors.UnAuthorizedError -> ApiCallState.UnAuthorizedError(response.message)
                is ResponseResultErrors.TimeOutError -> ApiCallState.TimeOutError(response.message)
                is ResponseResultErrors.UnknownError -> ApiCallState.UnknownError(response.message)
                else -> ApiCallState.UnknownError()
            }

    }

//    fun onError(response: ApiBaseResponse?): ApiCallState {
//
//        val apiCallResult: ApiCallState = ApiCallState.ResponseError(response)
//
////        if (showOutMessage)
//            apiEvents.value = apiCallResult;
//
//        return apiCallResult
//    }

    protected fun responseToViewEventMapper(it: ResponseResultWithWrapper<ResponseWrapper<Any>>) {
        if(it is ResponseResultWithWrapper.Success) {
            it.responseWrapper.data?.let { it1 ->  apiEvents.value =
                if (it1 is ApiBaseResponse)
                    ApiCallState.Loaded(it1)
                else
                    ApiCallState.Loaded()
            }
        }
        if(it is ResponseResultWithWrapper.Loading)
            apiEvents.value = ApiCallState.Loading

        if(it is ResponseResultWithWrapper.Error)
            errorMapper(it.responseWrapper.throwable)
        if(it is ResponseResultWithWrapper.ErrorResponse)
            apiEvents.value = ApiCallState.ResponseError(it.responseWrapper.responseError)
    }

    protected fun responseToViewEventMapper(it: ResponseResult<Any>) {
        if(it is ResponseResult.Success) {
            it.response.let { it1 ->  apiEvents.value =
                if (it1 is ApiBaseResponse)
                    ApiCallState.Loaded(it1)
                else
                    ApiCallState.Loaded()
            }
        }
        if(it is ResponseResult.Loading)
            apiEvents.value = ApiCallState.Loading
    }

//    fun onError(throwable: Throwable, showOutMessage: Boolean): ApiCallState {
//        val apiCallResult :ApiCallState
//        if (throwable is HttpException) {
//            val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())
//
//            if (message.httpCode == 0)
//                message.httpCode = throwable.code();
//
//
//            if (throwable.code() == 403 || throwable.code() == 401)
//                apiCallResult = UnAuthorizedError(message)
//            else if (throwable.code() == 404 || throwable.code() == 500)
//                apiCallResult = ResponseError(message)
//            else {
//                apiCallResult = ResponseError(message)
//            }
//
//        } else if (throwable is SocketTimeoutException) {
//            apiCallResult = TimeOutError(throwable)
//        } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
//            apiCallResult = NetworkError(throwable)
//        } else {
//            Log.e("errorrrrrr ", throwable.message);
//            apiCallResult = UnknownError(throwable)
//        }
//
//        if (showOutMessage)
//            apiEvents.value = apiCallResult;
//
//        return  apiCallResult
//    }
//
//    fun onResponse(response: ApiBaseResponse, showOutMessage: Boolean): ApiCallState {
//        Log.e("successfullCall: ", response.httpCode.toString());
//        val apiCallResult :ApiCallState
//        if (response.httpCode == Constants.CODE_SUCCESS) {
//            response.success = true;
//
//            response.showType = MessageShowType.TOAST
//            apiCallResult = Loaded(response) //ApiCallEvent(callState= ApiCallState.LOADED,message=response)
//
//        } else {
//            apiCallResult = ResponseError(response)//ApiCallEvent(callState= ApiCallState.LOADED,message=response)
//        }
//
//        if (showOutMessage)
//            apiEvents.value = Loaded(response)
//
//        return apiCallResult
//    }
//
//    fun isEmptyList(total: Int, messageStr: Int) {
//        if (total == 0) {
//            listLoadingState.set(ListError("empty list"))
//        }
//    }


}
