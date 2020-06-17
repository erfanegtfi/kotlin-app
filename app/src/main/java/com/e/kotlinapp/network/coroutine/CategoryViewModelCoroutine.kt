package com.e.kotlinapp.network.coroutine;

import android.app.Application;
import android.content.Context
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.local.IkoponDatabase
import com.e.kotlinapp.model.Category

import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.network.api.ApiClient
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CategoryViewModelCoroutine @Inject constructor(application: Application, private val categoryService: CategoryRepository) : BaseViewModel(application) {
//    fun <T> getApiClient(serviceClass: Class<T>): T {
//        return ApiClient.retrofit.create(serviceClass)
//    }
//    private var categoryService: CategoryRepository = CategoryRepository(getApiClient(ApiInterfaceCoroutine::class.java), IkoponDatabase.getInstance(application).getCategoryDao())


    fun getCategoryList() = categoryService.getCategoryListRemote(true)


    /////////////////////////////////////////////////////
    var categoryList: PublishSubject<MutableList<Category>> = PublishSubject.create();
    fun getCategoryList2(context: Context) {
        Coroutine.io {
            apiEvents.postValue(ResponseResult.Loading)
            var response = categoryService.getCategoryList2(true)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResponseResult.Success -> {
                        categoryList.onNext(response.response.data)
                    }
                }
                apiEvents.value = response
            }

        }
    }

    fun getCategoryList3(context: Context) {
        apiEvents.postValue(ResponseResult.Loading)
        Coroutine.ioThenMain({
            categoryService.getCategoryList2(true)
        }) {
            it?.let {
                when (it) {
                    is ResponseResult.Success -> {
                        categoryList.onNext(it.response.data)
                    }
                }
                apiEvents.value = it
            }
        }
    }
}