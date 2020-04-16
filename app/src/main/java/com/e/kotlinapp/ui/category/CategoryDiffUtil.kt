package com.e.kotlinapp.ui.category

import androidx.recyclerview.widget.DiffUtil
import com.e.kotlinapp.model.Category

class ListItemCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.catSlug == newItem.catSlug
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.catSlug == newItem.catSlug
    }
}