package com.example.memestreamproto.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gif(
    val id: String,
    val title : String,
    val images: ImageVariants
)
