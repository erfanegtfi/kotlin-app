package com.e.kotlinapp.di


import android.app.Application
import androidx.room.Room
import com.e.kotlinapp.BuildConfig
import com.e.kotlinapp.local.IkoponDatabase
import com.e.kotlinapp.network.NetInterceptor
import com.e.kotlinapp.network.api.ApiClient
import com.e.kotlinapp.network.api.ApiInterface
import com.e.kotlinapp.network.coroutine.ApiInterfaceCoroutine
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.annotations.NonNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    private  val BASE_URL = "https://apid.ikopon.ir/"
    private val BASE_IMAGE_URL = "https://apid.ikopon.ir/"
    private  val REQUEST_TIMEOUT_DURATION = 30

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

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

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create()
    }


    @Provides
    @Singleton
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient, gson: Gson ): Retrofit {

        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .addConverterFactory(ScalarsConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    @Provides
    @Singleton
    open fun provideApiService(retrofit: Retrofit): ApiInterfaceCoroutine = retrofit.create(ApiInterfaceCoroutine::class.java)

    @Provides
    @Singleton
    open fun provideApiService2(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@androidx.annotation.NonNull application: Application): IkoponDatabase {
        return IkoponDatabase.getInstance(application)
    }

}