package com.e.kotlinapp.ui.category;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.viewModelScope
import com.e.kotlinapp.BaseViewModel
import com.e.kotlinapp.model.Category
import com.ikopon.dataSource.networkServices.CategoryService

import com.e.kotlinapp.model.response.base.*
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class CategoryViewModel  @Inject constructor (application: Application, private val categoryService: CategoryService) : BaseViewModel(application) {

//    private var categoryService: CategoryService = CategoryService(this)
    var categoryList: PublishSubject<MutableList<Category>> = PublishSubject.create();


    @SuppressLint("CheckResult")
    fun getCategoryList() {
        listLoadingState.set(ListLoading);
        categoryService.getCategoryList().doOnError {
            listLoadingState.set(ListError(""))

        }.subscribe({ response ->
            listLoadingState.set(ListLoaded)
            categoryList.onNext(response.data)

        }, { it.printStackTrace() })

    }

}