package com.example.memestreamproto.data


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageVariants(
    val original: ImageInfo,
    val downSized: ImageInfo
)
