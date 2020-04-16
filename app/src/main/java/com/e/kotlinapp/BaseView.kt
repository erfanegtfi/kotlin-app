package com.e.kotlinapp;

import com.e.kotlinapp.model.response.base.ApiBaseResponse


interface BaseView {

    fun showLoading(message: String?)

    fun showLoading()

    fun hideLoading()

    fun unauthorizedUser(response: ApiBaseResponse?)

    fun onTimeout(throwable: Throwable?)

    fun onNetworkError(throwable: Throwable?)

    fun onError(throwable: Throwable?, response: ApiBaseResponse?)

    fun onResponseMessage(message: ApiBaseResponse?)

}