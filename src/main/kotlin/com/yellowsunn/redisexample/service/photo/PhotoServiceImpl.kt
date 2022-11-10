package com.yellowsunn.redisexample.service.photo

import com.yellowsunn.redisexample.http.Photo
import com.yellowsunn.redisexample.http.client.PhotoHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration

@Service
class PhotoServiceImpl(
    private val photoHttpClient: PhotoHttpClient,
    private val redisTemplate: ReactiveRedisTemplate<String, Photo>,
) : PhotoService {
    private val logger: Logger = LoggerFactory.getLogger(PhotoServiceImpl::class.java)

    override fun getPhoto(photoId: Long): Mono<Photo> {
        return redisTemplate.opsForValue().get(photoId.toString())
            .doOnNext { logger.info("Cached data={}, key={}", it, photoId) }
            .switchIfEmpty(
                photoHttpClient.getPhotoById(photoId)
                    .doOnNext { logger.info("Get data from httpClient. data={}", it) }
                    .publishOn(Schedulers.boundedElastic())
                    .flatMap { todo ->
                        logger.info("Set cache data={}. key={}", todo, photoId)
                        redisTemplate.opsForValue().set(photoId.toString(), todo, Duration.ofMinutes(5L))
                            .map { todo }
                    }
            )
    }
}