package kfd.experimentalservice.component

import io.micrometer.observation.annotation.Observed
import kfd.experimentalservice.model.User
import kfd.experimentalservice.repository.UserDao
import org.springframework.stereotype.Component
import java.lang.Thread.sleep

@Component
@Observed(name = "UserService")
class UserService(
    private val dao: UserDao
) {
    fun list(): Iterable<User> {
        sleep(100)
        return dao.findAll()
    }
}