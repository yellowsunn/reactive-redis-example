package com.yellowsunn.redisexample.service.photo

import com.yellowsunn.redisexample.http.Photo
import reactor.core.publisher.Mono

interface PhotoService {
    fun getPhoto(photoId: Long): Mono<Photo>
}