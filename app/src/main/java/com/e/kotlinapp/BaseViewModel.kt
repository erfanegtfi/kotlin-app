package com.e.kotlinapp;

import android.app.Application;
import android.util.Log;
import androidx.databinding.ObservableField

import androidx.lifecycle.AndroidViewModel;
import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.network.NoConnectivityException
import com.e.kotlinapp.network.UtilsError
import com.e.kotlinapp.model.response.base.ApiCallState.*
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException


import java.net.SocketTimeoutException;


open class BaseViewModel : AndroidViewModel {

    var listLoadingState: ObservableField<ListLoadState> =  ObservableField(ListInitial)
    var apiEvents: SingleLiveEvent<ApiCallState> = SingleLiveEvent()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()


    constructor (application: Application) : super(application)

    open fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onError(throwable: Throwable, showOutMessage: Boolean): ApiCallState {
        val apiCallResult :ApiCallState
        if (throwable is HttpException) {
            val message: ApiBaseResponse = UtilsError.parseError(throwable.response()?.errorBody())

            if (message.httpCode == 0)
                message.httpCode = throwable.code();


            if (throwable.code() == 403 || throwable.code() == 401)
                apiCallResult = UnAuthorizedError(throwable, message)
            else if (throwable.code() == 404 || throwable.code() == 500)
                apiCallResult = ResponseError(throwable, message)
            else {
                apiCallResult = ResponseError(throwable, message)
            }

        } else if (throwable is SocketTimeoutException) {
            apiCallResult = TimeOutError(throwable)
        } else if (throwable is NoConnectivityException) {//|| throwable instanceof IOException
            apiCallResult = NetworkError(throwable)
        } else {
            Log.e("errorrrrrr ", throwable.message);
            apiCallResult = UnknownError(throwable)
        }

        if (showOutMessage)
            apiEvents.value = apiCallResult;

        return  apiCallResult
    }

    fun onResponse(response: ApiBaseResponse, showOutMessage: Boolean): ApiCallState {
        Log.e("successfullCall: ", response.httpCode.toString());
        val apiCallResult :ApiCallState
        if (response.httpCode == Constants.CODE_SUCCESS) {
            response.success = true;

            response.showType = MessageShowType.TOAST
            apiCallResult = Loaded(response) //ApiCallEvent(callState= ApiCallState.LOADED,message=response)

        } else {
            apiCallResult = ResponseError(Throwable("serverrr errorrr"), response)//ApiCallEvent(callState= ApiCallState.LOADED,message=response)
        }

        if (showOutMessage)
            apiEvents.value = Loaded(response)

        return apiCallResult
    }


//    public void isEmptyList(List data, int message) {
//        if (data.isEmpty()) {
//            getMessage().set(message);
//            getLoading().set(ERROR);
//        }
//    }

    fun isEmptyList(total: Int, messageStr: Int) {
        if (total == 0) {
            listLoadingState.set(ListError("empty list"))
        }
    }

//    fun <T : LoadableList> void appendLoading(T obj, List<T> data) {
//        if (!data.isEmpty()) {
//            obj.setLoading(true);
//            data.add(obj);
//        }
//    }
}
