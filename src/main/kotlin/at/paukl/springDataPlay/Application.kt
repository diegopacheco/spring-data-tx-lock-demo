package at.paukl.springDataPlay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@SpringBootApplication
@EnableJdbcRepositories
class SpringDataPlayApplication

fun main(args: Array<String>) {
	runApplication<SpringDataPlayApplication>(*args)
}
