package com.mabdelhamid.kiwetask.data.network

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.mabdelhamid.kiwetask.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkManager {
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    fun init() {
        val loggingInterceptor = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .addHeader("version", BuildConfig.VERSION_NAME)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

        retrofit = Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .client(client.build())
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    operator fun invoke(): ApiService {
        return apiService
    }
}