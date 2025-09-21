package com.example.memestreamproto.apiService

import com.example.memestreamproto.data.GiphyResponse
import retrofit2.http.GET

//Site:https://www.geeksforgeeks.org/kotlin/retrofit-with-kotlin-coroutine-in-android/
interface ApiService {
//    @GET("/trending")
//    suspend fun getTrending(
//        @Query("api_key") apiKey: String,
//        @Query("limit") limit: Int = 5
//    ): GiphyResponse

    @GET("v1/gifs/trending?api_key=o1TpgXB3C2OY4XhmeRNDLbGROQUiy0DO&limit=15")
    suspend fun getTrending(): GiphyResponse
}