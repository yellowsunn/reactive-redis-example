package com.yellowsunn.redisexample.http.client

import com.yellowsunn.redisexample.http.Photo
import reactor.core.publisher.Mono

interface PhotoHttpClient {
    fun getPhotoById(id: Long): Mono<Photo>
}