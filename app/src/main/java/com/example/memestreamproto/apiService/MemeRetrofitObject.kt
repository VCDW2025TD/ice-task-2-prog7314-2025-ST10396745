package com.example.memestreamproto.apiService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MemeRetrofitObject {
    val baseUrl = "https://memestream-restfulapi.onrender.com/"

    val instance: MemeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MemeApiService::class.java)
    }
}