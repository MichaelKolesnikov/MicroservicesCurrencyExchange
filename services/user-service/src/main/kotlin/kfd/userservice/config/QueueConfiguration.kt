package kfd.userservice.config

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfiguration {
    @Bean
    fun someQueue(): Queue {
        return Queue("some-key")
    }
}