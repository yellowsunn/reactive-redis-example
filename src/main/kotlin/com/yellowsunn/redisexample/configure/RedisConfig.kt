package com.yellowsunn.redisexample.configure

import com.fasterxml.jackson.databind.ObjectMapper
import com.yellowsunn.redisexample.http.Photo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun reactiveRedisTemplate(
        redisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, Photo> {
        val keySerializer = RedisSerializer.string()
        val valueSerializer = Jackson2JsonRedisSerializer(Photo::class.java)
        valueSerializer.setObjectMapper(ObjectMapper())

        val serializationContext = RedisSerializationContext.newSerializationContext<String, Photo>()
            .key(keySerializer)
            .value(valueSerializer)
            .hashKey(keySerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(redisConnectionFactory, serializationContext)
    }
}