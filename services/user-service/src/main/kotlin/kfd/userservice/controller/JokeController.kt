package kfd.userservice.controller

import io.micrometer.observation.annotation.Observed
import kfd.userservice.service.JokeService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Observed(name = "JokeController")
class JokeController(
    private val jokeService: JokeService
) {
    @GetMapping("/jokes")
    fun getJokes() = ResponseEntity.ok(jokeService.getJokes())
}