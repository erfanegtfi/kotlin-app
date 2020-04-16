package com.e.kotlinapp.network.coroutine

import com.e.kotlinapp.model.response.base.ApiBaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

object Coroutine {

    fun makeCall(call: suspend (() -> Unit)) = CoroutineScope(Dispatchers.IO).launch {
        call()
    }
}