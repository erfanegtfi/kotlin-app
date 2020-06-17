package com.e.kotlinapp.ui.category;

import android.annotation.SuppressLint
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
import com.e.kotlinapp.ui.base.BaseFragment
import kotlinx.coroutines.launch

class CategoryFragmentCoroutine : BaseFragment<FragmentCategoriesBinding, CategoryViewModelCoroutine>() {

    private var categories: MutableList<Category> = mutableListOf()
    private lateinit var categoryAdapter: CategoryAdapter

    override var layoutRes = R.layout.fragment_categories
    override var viewModelClass = CategoryViewModelCoroutine::class.java
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
//        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (categories.isEmpty()) reloadNews();
    }

    private fun onItemClick(position: Int, view: View) {
//        ProductListActivity.start(context, this.categories.get(position));
    }

    @SuppressLint("CheckResult")
    private fun reloadNews() {
        viewModel.getCategoryList2(contextFragment)
        viewModel.categoryList.subscribe {
            this.categories.addAll(it);
            categoryAdapter.notifyDataSetChanged();
        }
/////////////////////////////////////////////////////////////////
//        viewModel.getCategoryList().observe(viewLifecycleOwner, Observer {
//            when (it) {
//                is ResponseResult.Success -> {
//                    this.categories.addAll(it.response.data);
//                    categoryAdapter.notifyDataSetChanged();
//
//                    Log.v("aaaaaaaa", "" + it.response.data.get(0).catSlug)
//                }
//                is ResponseResult.Loading -> {
//                    binding.recycleView.loadingState(ListLoading)
//                }
//            }
//
//        });
    }

}