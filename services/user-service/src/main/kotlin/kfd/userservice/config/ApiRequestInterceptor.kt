package kfd.userservice.config

import io.github.cdimascio.dotenv.dotenv
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiRequestInterceptor {
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor {
            requestTemplate -> requestTemplate.header("X-Api-Key", dotenv()["NINJAS_API_KEY"])
        }
    }
}