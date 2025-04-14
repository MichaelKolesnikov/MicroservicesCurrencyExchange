package kfd.userservice.controller

import kfd.userservice.service.JokeService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class JokeController(
    private val jokeService: JokeService
) {
    @GetMapping("/jokes")
    fun getJokes() = ResponseEntity.ok(jokeService.getJokes())
}