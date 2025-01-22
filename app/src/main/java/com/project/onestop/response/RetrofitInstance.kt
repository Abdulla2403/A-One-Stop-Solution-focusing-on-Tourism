package com.project.onestop.response

import android.util.Base64
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val AUTH = "Basic" + Base64.encodeToString("sss".toByteArray(), Base64.NO_WRAP)
    private const val BASE_URL = "https://wizzie.online/CraftLeather/"
    const val TYPE = "oneStop1"

    private val okHttp = okhttp3.OkHttpClient.Builder()
        .addInterceptor { interceptorChain ->
            val original = interceptorChain.request()
            val build = original.newBuilder()
                .method(original.method, original.body)
                .addHeader("Authorization", AUTH)
            val request = build.build()
            interceptorChain.proceed(request)
        }.build()

    val instance: API by lazy {
        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(API::class.java)
    }

}