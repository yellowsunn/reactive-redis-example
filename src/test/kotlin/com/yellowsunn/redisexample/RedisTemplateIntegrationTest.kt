package com.yellowsunn.redisexample

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import java.time.Duration

@SpringBootTest
class RedisTemplateIntegrationTest: AbstractIntegrationTest() {
    @Autowired
    lateinit var reactiveRedisTemplate: ReactiveRedisTemplate<String, String>

    @Test
    fun test() = runTest {
        reactiveRedisTemplate.opsForValue().set("test", "example", Duration.ofSeconds(10L))
            .awaitSingle()

        val retrieved = reactiveRedisTemplate.opsForValue().get("test")
            .awaitSingle()

        assertThat(retrieved).isEqualTo("example")
    }
}