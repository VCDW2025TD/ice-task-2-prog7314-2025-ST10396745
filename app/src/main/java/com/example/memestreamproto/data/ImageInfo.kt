package com.example.memestreamproto.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageInfo(
    val url : String
)
