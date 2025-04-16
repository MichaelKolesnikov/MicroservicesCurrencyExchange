package kfd.userservice.controller

import kfd.userservice.model.SomeMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class QueueController(
    private val rabbitTemplate: RabbitTemplate,
) {
    @PostMapping("/send")
    fun send(@RequestBody message: SomeMessage): ResponseEntity<String> {
        rabbitTemplate.convertAndSend("some-key", message)
        return ResponseEntity.ok("Sent!")
    }
}