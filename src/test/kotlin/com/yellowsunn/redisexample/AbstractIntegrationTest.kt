package com.yellowsunn.redisexample

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@ActiveProfiles("test")
abstract class AbstractIntegrationTest {
    companion object {
        private const val REDIS_IMAGE = "redis:6.0.5-alpine"

        private val redisContainer = GenericContainer(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(6379)

        @JvmStatic
        @DynamicPropertySource
        fun redisProperties(registry: DynamicPropertyRegistry) {
            redisContainer.start()
            registry.add("spring.redis.host") { redisContainer.host }
            registry.add("spring.redis.port") { redisContainer.firstMappedPort.toString() }
        }
    }
}