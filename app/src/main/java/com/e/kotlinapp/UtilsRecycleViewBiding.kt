package com.e.kotlinapp

import androidx.databinding.BindingAdapter
import com.e.kotlinapp.model.response.base.ListLoadState


//https://softwarebrothers.co/blog/bindingadapter/
//https://android.jlelse.eu/android-data-binding-binding-adapters-part-5-2bc91e43caa0

//https://softwarebrothers.co/blog/bindingadapter/
//https://android.jlelse.eu/android-data-binding-binding-adapters-part-5-2bc91e43caa0

@BindingAdapter("loadingState")
fun RecycleViewWidget.loadingState(listLoadState: ListLoadState) {
    setLoadingState(listLoadState)
}

