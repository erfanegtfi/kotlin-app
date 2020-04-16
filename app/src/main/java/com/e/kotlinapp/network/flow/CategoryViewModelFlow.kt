package com.e.kotlinapp.network.flow;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.local.IkoponCategoryDatabase
import com.e.kotlinapp.local.dao.CategoryDao
import com.e.kotlinapp.model.Category
import com.ikopon.dataSource.networkServices.CategoryService

import com.e.kotlinapp.model.response.base.*
import com.e.kotlinapp.network.ApiClient
import com.e.kotlinapp.network.coroutine.*
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

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