package kfd.experimentalservice.controller

import io.micrometer.observation.annotation.Observed
import kfd.experimentalservice.component.UserService
import kfd.experimentalservice.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep

@RestController
@Observed(name = "UserController")
@RequestMapping("/users")
class UserController(
    private val service: UserService
) {
    @GetMapping("/")
    fun index(): Iterable<User> {
        sleep(100)
        return service.list()
    }
}