package com.e.kotlinapp.model.response.base;


sealed class ListLoadState
object ListInitial : ListLoadState()
object ListLoading : ListLoadState()
object ListLoaded : ListLoadState()
data class ListError(val message: String) : ListLoadState()
