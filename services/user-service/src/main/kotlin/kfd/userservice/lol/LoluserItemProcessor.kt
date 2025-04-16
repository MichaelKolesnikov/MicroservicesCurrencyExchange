package kfd.userservice.lol

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor

class LoluserItemProcessor : ItemProcessor<Loluser, Loluser> {
    private val log = LoggerFactory.getLogger(LoluserItemProcessor::class.java)

    override fun process(loluser: Loluser): Loluser {
        val name = loluser.name?.uppercase()
        val email = loluser.email?.uppercase()
        return Loluser(name, email).also { log.info("Converting $loluser into $it") }
    }
}
