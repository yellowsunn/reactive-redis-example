package com.yellowsunn.redisexample.http.client

import com.yellowsunn.redisexample.http.Photo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class PhotoHttpWebClient : PhotoHttpClient {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()

    override fun getPhotoById(id: Long): Mono<Photo> {
        return this.webClient.get()
            .uri("/photos/{id}", id)
            .retrieve()
            .bodyToMono()
    }
}