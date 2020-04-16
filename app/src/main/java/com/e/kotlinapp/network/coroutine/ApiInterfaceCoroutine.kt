package com.e.kotlinapp.network.coroutine;


import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.ApiListResponse
import io.reactivex.Observable;
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET;

/**
 * This interface is for api request that fire in App.java
 */
interface ApiInterfaceCoroutine {

    //-------------------------------- category --------------------------------
    @GET("public/category/list/")
    suspend fun getCategoryList(): Response<ApiListResponse<Category>>


}
