package com.e.kotlinapp.network

import com.e.kotlinapp.model.response.base.ApiBaseResponse
import com.e.kotlinapp.network.api.ApiClient
import okhttp3.ResponseBody
import java.io.IOException

object UtilsError {

    fun parseError(responseBody: ResponseBody?): ApiBaseResponse {
        val converter = ApiClient.retrofit.responseBodyConverter<ApiBaseResponse>(
            ApiBaseResponse::class.java, arrayOfNulls(0)
        )

        val error: ApiBaseResponse?
        error = try {
            converter.convert(responseBody ?: ResponseBody.create(null, ""))
        } catch (e: IOException) {
            return ApiBaseResponse()
        }
        return error!!
    }
}