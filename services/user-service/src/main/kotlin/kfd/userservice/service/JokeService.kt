package kfd.userservice.service

import io.micrometer.observation.annotation.Observed
import kfd.userservice.model.Joke
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@Observed(name = "JokeService")
@FeignClient(name = "joke-service", url = "https://api.api-ninjas.com/v1")
interface JokeService {
    @GetMapping("/jokes")
    fun getJokes(): List<Joke>
}