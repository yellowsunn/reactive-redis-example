package com.yellowsunn.redisexample.controller.photo

import com.yellowsunn.redisexample.http.Photo
import com.yellowsunn.redisexample.service.photo.PhotoService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class PhotoController(
    private val photoService: PhotoService,
) {
    private val logger: Logger = LoggerFactory.getLogger(PhotoController::class.java)

    @GetMapping("/photos/{id}")
    fun test(@PathVariable id: Long): Mono<ResponseEntity<Photo>> {
        return photoService.getPhoto(id)
            .map { ResponseEntity(it, HttpStatus.OK) }
            .doOnError { e -> logger.error("message={}", e.message, e) }
    }
}