package org.ablonewolf.coursecatalogservice.controller

import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class GreetingControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun retrieveGreeting() {
        val name = "Arka Bhuiyan"

        val result = webTestClient.get()
            .uri("/greetings?name=$name")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()

        Assertions.assertEquals("Hello there $name, welcome to the Course Catalog Service!", result.responseBody)
    }
}
