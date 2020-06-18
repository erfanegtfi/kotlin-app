package com.e.kotlinapp;

import com.e.kotlinapp.model.response.base.ApiBaseResponse


interface BaseView {

    fun showLoading(message: String?)

    fun showLoading()

    fun hideLoading()

    fun unauthorizedUser(response: ApiBaseResponse?)

    fun onTimeout(throwable: String?)

    fun onNetworkError(throwable: String?)

    fun onError(throwable: String?)

    fun onResponseMessage(message: ApiBaseResponse?)

}