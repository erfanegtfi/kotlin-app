package com.e.kotlinapp.network.api.apiFactory

import com.e.kotlinapp.network.coroutine.ApiInterfaceCoroutine


//https://github.com/SimpleBoilerplates/Android/blob/master/app/src/main/java/com/me/booksdemoandroid/shared/repositories/ApiFactory.kt
object  ApiFactory {

    val authApi = RetrofitFactory.retrofit()
        .create(ApiInterfaceCoroutine::class.java)

    val homeApi = RetrofitFactory.retrofit()
        .create(ApiInterfaceCoroutine::class.java)

}