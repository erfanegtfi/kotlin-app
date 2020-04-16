package com.e.kotlinapp.network

import com.e.kotlinapp.BuildConfig
import com.e.kotlinapp.network.api.ApiInterface
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    const val BASE_URL = "https://apid.ikopon.ir/"
    const val BASE_IMAGE_URL = "https://apid.ikopon.ir/"
    private const val REQUEST_TIMEOUT_DURATION = 30


    val retrofit: Retrofit = Retrofit.Builder().run {
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create()

        baseUrl(BASE_URL)
        addConverterFactory(GsonConverterFactory.create(gson))
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        addConverterFactory(ScalarsConverterFactory.create())
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        client(createRequestInterceptorClient())

    }.build()

//    fun createRetrofitClient(): Retrofit {
//        return retrofit
//    }

    val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)

    fun <T> getApiClient(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    private fun createRequestInterceptorClient(): OkHttpClient {
//        val interceptor = Interceptor { chain ->
//            val original = chain.request()
//            val requestBuilder = original.newBuilder()
//            val request = requestBuilder.build()
//            chain.proceed(request)
//        }

        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(NetInterceptor())
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(NetInterceptor())
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        }
    }
}