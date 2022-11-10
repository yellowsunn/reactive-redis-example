package com.yellowsunn.redisexample.service.photo

import com.yellowsunn.redisexample.http.Photo
import com.yellowsunn.redisexample.http.client.PhotoHttpClient
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class PhotoServiceImpl(
    private val photoHttpClient: PhotoHttpClient,
    private val redisTemplate: ReactiveRedisTemplate<String, Photo>,
) : PhotoService {
    private val logger: Logger = LoggerFactory.getLogger(PhotoServiceImpl::class.java)

    override fun getPhotoByReactor(photoId: Long): Mono<Photo> {
        return redisTemplate.opsForValue().get(photoId.toString())
            .doOnNext { logger.info("Cached data={}, key={}", it, photoId) }
            .switchIfEmpty(
                photoHttpClient.getPhotoById(photoId)
                    .doOnNext { logger.info("Get data from httpClient. data={}", it) }
                    .flatMap { photo: Photo ->
                        redisTemplate.opsForValue().set(photoId.toString(), photo, Duration.ofMinutes(5L))
                            .doOnNext { logger.info("Set cache data={}. key={}", photo, photoId) }
                            .map { photo }
                    }
            )
    }

    override suspend fun getPhotoByCoroutine(photoId: Long): Photo {
        val cachedPhoto: Photo? = redisTemplate.opsForValue().get(photoId.toString())
            .awaitSingleOrNull()
        if (cachedPhoto != null) {
            logger.info("Cached data={}, key={}", cachedPhoto, photoId)
            return cachedPhoto
        }

        val photo: Photo = photoHttpClient.getPhotoById(photoId)
            .awaitSingle()
        logger.info("Get data from httpClient. data={}", photo)

        redisTemplate.opsForValue().set(photoId.toString(), photo, Duration.ofMinutes(5L))
            .awaitSingle()
        logger.info("Set cache data={}. key={}", photo, photoId)

        return photo
    }
}