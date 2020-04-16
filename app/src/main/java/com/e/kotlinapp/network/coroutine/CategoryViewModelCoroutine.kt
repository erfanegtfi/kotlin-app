package com.e.kotlinapp.network.coroutine;

import android.app.Application;
import android.content.Context
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.local.IkoponCategoryDatabase
import com.e.kotlinapp.model.Category

import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.network.ApiClient
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CategoryViewModelCoroutine(application: Application) : BaseViewModel(application) {
    fun <T> getApiClient(serviceClass: Class<T>): T {
        return ApiClient.retrofit.create(serviceClass)
    }

    private var categoryService: CategoryRepository = CategoryRepository(getApiClient(ApiInterfaceCoroutine::class.java), IkoponCategoryDatabase.getInstance(application).getPostsDao())


    fun getCategoryList() = categoryService.getCategoryListRemote(true)


    /////////////////////////////////////////////////////
    var categoryList: PublishSubject<MutableList<Category>> = PublishSubject.create();
    fun getCategoryList2(context: Context) {
        Coroutine.makeCall {
            apiEvents.postValue(ApiCallState.Loading)
            var response = categoryService.getCategoryList2(true)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResponseResult.Success -> {
                        categoryList.onNext(response.response.data)
                        apiEvents.value = ApiCallState.Loaded(response.response)
                    }
                }
            }

        }
    }


}