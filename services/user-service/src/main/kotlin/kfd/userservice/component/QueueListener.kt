package kfd.userservice.component

import kfd.userservice.model.SomeMessage
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

// curl http://localhost:8000/user-service/send -d '{"author": "John Doe", "text": "Hello world!"}' -H "Content-Type: application/json"
@EnableRabbit
@Component
class QueueListener {
    @RabbitListener(queues = ["some-key"])
    fun listen(message: SomeMessage) {
        println("Message read from ${message.author} myQueue: ${message.text}")
    }
}