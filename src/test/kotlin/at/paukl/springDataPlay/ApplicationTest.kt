package at.paukl.springDataPlay

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApplicationTest {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `Test user signup`() {
        // NOTE: this will fail because of the blocking waiting for a new connection
        //  when the db-pool has actually run out (we try 6 parallel requests, each request uses 2 connections
        callUrl("/signup")
    }

    @Test
    fun `Test user signup fixed`() {
        callUrl("/signupFixed")
    }

    private fun callUrl(url: String) {
        println("HTTP call start")

        val result = restTemplate.postForEntity("http://localhost:$port$url", "", String::class.java)
        println("HTTP call finish")

        assertThat(result)
            .`as`("request must succeed with 200 OK")
            .extracting({ it.statusCodeValue })
            .isEqualTo(200)
    }

}
