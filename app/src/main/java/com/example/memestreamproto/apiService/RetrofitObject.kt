package com.example.memestreamproto.apiService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Site: https://www.geeksforgeeks.org/kotlin/retrofit-with-kotlin-coroutine-in-android/
object RetrofitObject {

    val baseUrl = "https://api.giphy.com/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory((GsonConverterFactory.create()))
            .build()

    }
}