package kfd.userservice.lol

import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BatchConfig {
    @Bean
    fun importLolUsersJob(
        jobRepository: JobRepository,
        step1: Step
    ): Job {
        return JobBuilder("importLolUsersJob", jobRepository)
            .start(step1)
            .build()
    }

    @Bean
    fun step1(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        writer: JdbcBatchItemWriter<Loluser?>
    ): Step {
        return StepBuilder("step1", jobRepository)
            .chunk<Loluser, Loluser>(10, transactionManager)
            .reader(ReaderWriterConfig().reader())
            .processor(LoluserItemProcessor())
            .writer(writer)
            .build()
    }
}