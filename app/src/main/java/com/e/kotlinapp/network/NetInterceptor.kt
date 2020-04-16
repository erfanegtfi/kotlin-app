package com.e.kotlinapp.network

import android.util.Log
import com.e.kotlinapp.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class NetInterceptor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
//        val network: Boolean = Utils.isConnectingToInternet(mContext)
//        if (!network) {
//            throw NoConnectivityException()
//        }
//        val token: String = Session.getPreferenceValue(Session.TAG_USER_TOKEN, null)
        val chainn = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .header("Content-Type", "application/json")
//        if (!TextUtils.isEmpty(token)) chainn.header("Authorization", "JWT $token")
        //        chainn.header("Authorization", "JWT " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6Iis5ODkxMzc4NjYwODgiLCJleHAiOjE1ODQyNjQ1ODgsImVtYWlsIjoiIiwib3JpZ19pYXQiOjE1ODE2NzI1ODh9.2YNkUNP3lNhwK4P7WrHCLHHjLNa7hgzDUQyBQwCnBEM");
//        eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJ1c2VybmFtZSI6Iis5ODkxMzg5NzA4ODYiLCJleHAiOjE1ODQyNzI5MTgsImVtYWlsIjoiIiwib3JpZ19pYXQiOjE1ODE2ODA5MTh9.56GJkiME6zUyXW66S6YhXSKTph9PGYbSPvnZwxCaLjU
        val request = chainn.build()
        val response = chain.proceed(request)
        if (BuildConfig.DEBUG) {
            val t1 = System.nanoTime()
            val requestBodyString = bodyToString(request)
            var requestLog =
                "\n" + "---------------------------REQUEST:--------------------------" + "\n"
            requestLog += String.format(
                "Sending request %s on %s%n",
                request.url(),
                chain.connection()
            ) //info != null ? new Gson().toJson(info) :
            if (request.method().compareTo("post", ignoreCase = true) == 0) {
                requestLog = "\n" + requestLog + "\n" + requestBodyString
            }
            requestLog += "-------------------------------------------------------------" + "\n\n "
            Log.v("interceptor", ".\n$requestLog")
            val t2 = System.nanoTime()
            val responseLog = String.format(
                Locale.US,
                "Received response for %s in %.1fms%n",
                response.request().url(),
                (t2 - t1) / 1e6
            )
            var bodyString = ""
            try {
                val responseBody = response.body()
                val source = responseBody!!.source()
                source.request(Long.MAX_VALUE) // request the entire body.
                val buffer = source.buffer()
                // clone buffer before reading from it
                bodyString = buffer.clone().readString(Charset.forName("UTF-8"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bodyString = prettyJson(bodyString)
            var headerStr = " \n$responseLog"
            var requestStr = " \n"
            headerStr += "\n\nURL:" + request.url() + "\n"
            headerStr += "Headers:\n" + request.headers() + "\n"
            Log.w("interceptor:", "\n$headerStr\n\n ")
            //            requestStr += ".\n\nHeader:\n" + requestBodyString + "\n";
            requestStr += "Body:\n$bodyString"
            val chunkSize = 3500
            var i = 0
            while (i < requestStr.length) {
                Log.w(
                    "interceptor",
                    requestStr.substring(i, Math.min(requestStr.length, i + chunkSize))
                )
                i += chunkSize
            }
            //            Log.v("interceptor", requestStr);
        }
        return response
    }

    companion object {
        //
        fun bodyToString(request: Request): String {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body()?.writeTo(buffer)
                prettyJson(buffer.readUtf8())
            } catch (e: IOException) {
                "did not work"
            }
        }

        /**
         * Method make pretty format json
         */
        fun prettyJson(string: String): String {
            return try {
                val parser = JsonParser()
                val gson = GsonBuilder().setPrettyPrinting().create()
                val el = parser.parse(string)
                gson.toJson(el) // done
            } catch (e: JsonSyntaxException) {
                e.printStackTrace().toString()
            }
        }
    }

}