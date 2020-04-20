package com.e.kotlinapp.network.api.apiFactory


import com.e.kotlinapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object  RetrofitFactory {

    const val BASE_URL = "https://apid.ikopon.ir/"

    private val authInterceptor = Interceptor { chain ->

        val accessToken = ""//Pref.token

        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_key", "my_api_key")
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl.url())
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(newRequest)
    }


//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }

    private val headersInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }

    //Not logging the authkey if not debug
    private val client =
        if (BuildConfig.DEBUG) {
            OkHttpClient().newBuilder()
                .addInterceptor(authInterceptor)
//                .addInterceptor(loggingInterceptor)
                .addInterceptor(headersInterceptor)
                .build()
        } else {
            OkHttpClient().newBuilder()
//                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(headersInterceptor)
                .build()
        }

    fun getGson() = GsonBuilder()
        .enableComplexMapKeySerialization()
        .setPrettyPrinting()
        .create()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

}