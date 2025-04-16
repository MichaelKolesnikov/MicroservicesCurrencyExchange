package kfd.userservice.lol

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.sql.DataSource

@Configuration
class ReaderWriterConfig {

    @Bean
    fun reader(): FlatFileItemReader<Loluser> {
        return FlatFileItemReaderBuilder<Loluser>()
            .name("loluserItemReader")
            .resource(ClassPathResource("lolusers.csv"))
            .delimited()
            .names("name", "email")
            .fieldSetMapper(BeanWrapperFieldSetMapper<Loluser>().apply {
                setTargetType(Loluser::class.java)
            })
            .build()
    }

    @Bean
    fun processor(): LoluserItemProcessor {
        return LoluserItemProcessor()
    }

    @Bean
    fun writer(dataSource: DataSource): JdbcBatchItemWriter<Loluser> {
        return JdbcBatchItemWriterBuilder<Loluser>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql("INSERT INTO lolusers (name, email) VALUES (:name, :email)")
            .dataSource(dataSource)
            .build()
    }
}