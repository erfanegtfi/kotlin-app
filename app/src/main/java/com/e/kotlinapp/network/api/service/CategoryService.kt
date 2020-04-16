package com.ikopon.dataSource.networkServices;

import android.annotation.SuppressLint;
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.ApiListResponse
import com.e.kotlinapp.network.api.ApiInterface
import com.e.kotlinapp.network.api.service.BaseService
import com.e.kotlinapp.BaseViewModel


import io.reactivex.Single;


/**
 * Created by Erfan Eghterafi on 6/27/2018.
 * erfan.eghterafi@gmail.com
 */
class CategoryService(baseViewModel: BaseViewModel) : BaseService(baseViewModel) {


    @SuppressLint("CheckResult")
    fun  getCategoryList():Single<ApiListResponse<Category>> {
        return callApi(getApiClient(ApiInterface::class.java).getCategoryList())
    }

}
