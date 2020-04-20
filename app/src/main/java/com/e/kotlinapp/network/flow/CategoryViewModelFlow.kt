package com.e.kotlinapp.network.flow;

import android.app.Application;
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.local.IkoponCategoryDatabase
import com.e.kotlinapp.model.Category

import com.e.kotlinapp.network.api.ApiClient
import com.e.kotlinapp.network.coroutine.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CategoryViewModelFlow(application: Application) : BaseViewModel(application) {
    fun <T> getApiClient(serviceClass: Class<T>): T {
        return ApiClient.retrofit.create(serviceClass)
    }
    private val _postsLiveData = MutableLiveData<List<Category>>()

    val postsLiveData: LiveData<List<Category>>
        get() = _postsLiveData

    private var categoryService = CategoryRepositoryFlow(getApiClient(ApiInterfaceCoroutine::class.java), IkoponCategoryDatabase.getInstance(application).getPostsDao())

    //using flow
    fun getCategories() {
        viewModelScope.launch {
            Log.v("sdfasdasdfaaaaaa", Thread.currentThread().name)
            categoryService.loadCategory().collect {
                if(it is ResponseResultWithWrapper.Success)
                    _postsLiveData.value = it.responseWrapper.data
            }
        }
    }

    //live data
    fun getCategories2() =categoryService.loadCategory11()

}