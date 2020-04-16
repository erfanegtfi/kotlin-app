package com.e.kotlinapp.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import com.e.kotlinapp.BR
import com.e.kotlinapp.R
import com.e.kotlinapp.databinding.FragmentCategoriesBinding
import com.e.kotlinapp.model.Category
import com.e.kotlinapp.ui.base.BaseFragment


import io.reactivex.disposables.Disposable;

class CategoryFragment : BaseFragment<FragmentCategoriesBinding, CategoryViewModel>() {

    private var categories: MutableList<Category> = mutableListOf()
    private lateinit var categoryAdapter: CategoryAdapterDiffUtils

    override var layoutRes = R.layout.fragment_categories
    override var viewModelClass = CategoryViewModel::class.java
    override val bindingVariable = BR.viewModel

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var subscribe: Disposable = viewModel.categoryList.subscribe {
            categoryAdapter.submitList(categories);
        }

        binding.recycleView.setLinearLayoutManager();
        categoryAdapter = CategoryAdapterDiffUtils(contextFragment, R.layout.item_category, categories, ::onItemClick);
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

    private fun reloadNews() {
        viewModel.getCategoryList();
    }

}