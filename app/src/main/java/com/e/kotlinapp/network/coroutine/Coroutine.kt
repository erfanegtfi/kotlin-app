package com.e.kotlinapp.network.coroutine

import com.e.kotlinapp.model.response.base.ApiBaseResponse
import kotlinx.coroutines.*
import retrofit2.Response

//https://github.com/SimpleBoilerplates/Android/blob/master/app/src/main/java/com/me/booksdemoandroid/shared/extension/Coroutines.kt
object Coroutine {

//    fun makeCall(call: suspend (() -> Unit)) = CoroutineScope(Dispatchers.IO).launch {
//        call()
//    }


    fun <T : Any> io(work: suspend (() -> T?)): Job = CoroutineScope(Dispatchers.IO).launch {
        work()
    }


    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)? = null): Job = CoroutineScope(Dispatchers.Main).launch {
        val data = CoroutineScope(Dispatchers.IO).async {
            return@async work()
        }.await()
        callback?.let {
            it(data)
        }
    }

}