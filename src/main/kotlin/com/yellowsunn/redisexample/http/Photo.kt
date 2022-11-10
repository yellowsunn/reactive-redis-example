package com.yellowsunn.redisexample.http

import java.beans.ConstructorProperties

data class Photo @ConstructorProperties("albumId", "id", "title", "url", "thumbnailUrl") constructor(
    val albumId: Long,
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)