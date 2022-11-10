package com.yellowsunn.redisexample.service.photo

import com.yellowsunn.redisexample.http.Photo
import reactor.core.publisher.Mono

interface PhotoService {
    fun getPhotoByReactor(photoId: Long): Mono<Photo>
    suspend fun getPhotoByCoroutine(photoId: Long): Photo
}