package com.ikopon.dataSource.networkServices;


import android.annotation.SuppressLint
import android.app.Application
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.ApiListResponse
import com.e.kotlinapp.network.api.ApiInterface
import com.e.kotlinapp.network.api.service.BaseService
import io.reactivex.Single
import javax.inject.Inject


/**
 * Created by Erfan Eghterafi on 6/27/2018.
 * erfan.eghterafi@gmail.com
 */
class CategoryService @Inject constructor(): BaseService() {


    @SuppressLint("CheckResult")
    fun  getCategoryList():Single<ApiListResponse<Category>> {
        return callApi(getApiClient(ApiInterface::class.java).getCategoryList())
    }

}
