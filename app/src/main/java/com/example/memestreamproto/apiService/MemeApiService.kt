package com.example.memestreamproto.apiService

import com.example.memestreamproto.data.MemePost
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MemeApiService {
    @Headers("Content-Type: application/json")
    @POST("memes")
    suspend fun postMeme(
        @Body memePost: MemePost
    ): Response<MemePost>
}