package kfd.userservice.lol

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lol")
class LolController(
    private val jobLauncher: JobLauncher,
    private val importLolUsersJob: Job
) {

    @GetMapping("/kek")
    fun processLolUsers(): String {
        val jobParameters = JobParametersBuilder()
            .addLong("startAt", System.currentTimeMillis())
            .toJobParameters()

        jobLauncher.run(importLolUsersJob, jobParameters)

        return "LOL users processing started!"
    }
}