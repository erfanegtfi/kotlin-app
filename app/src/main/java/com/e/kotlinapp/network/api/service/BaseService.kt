package com.e.kotlinapp.network.api.service;

import android.annotation.SuppressLint;
import com.e.kotlinapp.network.api.ApiClient
import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Erfan Eghterafi on 2/28/2019.
 * erfan.eghterafi@gmail.com
 */

private val TAG: String = BaseService::class.java.simpleName

open class BaseService(private var baseViewModel: BaseViewModel) {


    //create retrofit objects for different api interfaces
    fun <T> getApiClient(serviceClass: Class<T>): T {
        return ApiClient.retrofit.create(serviceClass)
    }


    @SuppressLint("CheckResult")
    fun <T : ApiBaseResponse> callApi(observable: Single<T>): Single<T> {
        return observable.observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            baseViewModel.compositeDisposable.add(it)
        }.doOnDispose {}//.retryWhen { f -> f.take(3).delay(5, TimeUnit.SECONDS) }//tried 3 times with a 5 sec break }
            .doOnError {

            }.subscribeOn(Schedulers.io());
    }

}
