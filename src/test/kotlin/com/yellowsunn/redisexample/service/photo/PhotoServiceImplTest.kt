package com.yellowsunn.redisexample.service.photo

import com.yellowsunn.redisexample.http.Photo
import com.yellowsunn.redisexample.http.client.PhotoHttpClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.core.publisher.Mono
import java.time.Duration

internal class PhotoServiceImplTest {

    private lateinit var sut: PhotoServiceImpl
    private val photoHttpClient = mockk<PhotoHttpClient>()
    private val redisTemplate = mockk<ReactiveRedisTemplate<String, Photo>>()

    @BeforeEach
    internal fun setUp() {
        sut = PhotoServiceImpl(photoHttpClient, redisTemplate)
    }

    @Test
    fun getPhotoByReactor() {
        //given
        val photoId = 1L
        every { redisTemplate.opsForValue().get(photoId.toString()) } returns Mono.empty()
        every { redisTemplate.opsForValue().set(photoId.toString(), any(), any<Duration>()) } returns Mono.just(true)
        every { photoHttpClient.getPhotoById(photoId) } returns Mono.just(
            Photo(
                1L,
                photoId,
                "title",
                "http://example.com",
                "http://example.com"
            )
        )

        //when
        val photo: Photo? = sut.getPhotoByReactor(photoId)
            .block()

        //then
        assertEquals(photo?.id, photoId)
    }

    @Test
    fun getPhotoByCoroutine() = runTest {
        //given
        val photoId = 1L
        every { redisTemplate.opsForValue().get(photoId.toString()) } returns Mono.empty()
        every { redisTemplate.opsForValue().set(photoId.toString(), any(), any<Duration>()) } returns Mono.just(true)
        every { photoHttpClient.getPhotoById(photoId) } returns Mono.just(
            Photo(
                1L,
                photoId,
                "title",
                "http://example.com",
                "http://example.com"
            )
        )

        //when
        val photo: Photo = sut.getPhotoByCoroutine(photoId)

        //then
        assertEquals(photo.id, photoId)
    }
}