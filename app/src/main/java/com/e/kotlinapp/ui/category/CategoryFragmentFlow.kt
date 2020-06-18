package com.e.kotlinapp.ui.category;

import android.app.Application
import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.e.kotlinapp.BR
import com.e.kotlinapp.R
import com.e.kotlinapp.databinding.FragmentCategoriesBinding
import com.e.kotlinapp.di.component.DaggerAppComponent
import com.e.kotlinapp.loadingState
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.model.response.base.ListLoading
import com.e.kotlinapp.network.coroutine.CategoryViewModelCoroutine
import com.e.kotlinapp.network.coroutine.ResponseResult
import com.e.kotlinapp.network.coroutine.ResponseResultWithWrapper
import com.e.kotlinapp.network.flow.CategoryViewModelFlow
import com.e.kotlinapp.ui.base.BaseFragment
import kotlinx.coroutines.launch

class CategoryFragmentFlow : BaseFragment<FragmentCategoriesBinding, CategoryViewModelFlow>() {

    private var categories: MutableList<Category> = mutableListOf()
    private lateinit var categoryAdapter: CategoryAdapter

    override var layoutRes = R.layout.fragment_categories
    override var viewModelClass = CategoryViewModelFlow::class.java
    override val bindingVariable = BR.viewModel

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerAppComponent.factory().create(application = baseActivity.application).inject(this)
        super.onCreate(savedInstanceState);
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.recycleView.setLinearLayoutManager();
        categoryAdapter = CategoryAdapter(contextFragment, R.layout.item_category, categories, ::onItemClick);
        binding.recycleView.setAdapter(categoryAdapter);

//

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (categories.isEmpty()) reloadCategories();
    }

    private fun onItemClick(position: Int, view: View) {
    }

    private fun reloadCategories() {
        viewModel.getCategories()
        viewModel.postsLiveData.observe(viewLifecycleOwner, Observer {
            this.categories.clear()
            this.categories.addAll(it);
            categoryAdapter.notifyDataSetChanged();
        })

//        viewModel.getCategories2().observe(viewLifecycleOwner, Observer {
//            if (it is ResponseResultWithWrapper.Success) {
//                this.categories.clear()
//                it.responseWrapper.data?.let { it1 -> this.categories.addAll(it1) };
//                categoryAdapter.notifyDataSetChanged();
//            }
//        })
    }

}