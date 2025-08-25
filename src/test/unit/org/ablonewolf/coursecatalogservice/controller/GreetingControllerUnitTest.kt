package org.ablonewolf.coursecatalogservice.controller

import org.ablonewolf.coursecatalogservice.service.GreetingsService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [GreetingController::class])
class GreetingControllerUnitTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var greetingsService: GreetingsService

    @Test
    fun retrieveGreeting() {
        val name = "Arka Bhuiyan"
        val expectedMessage = "Hello there $name, welcome to the Course Catalog Service!"

        whenever(greetingsService.retrieveGreeting(name))
            .thenReturn(expectedMessage)

        mockMvc.perform(get("/greetings").param("name", name))
            .andExpect(status().isOk)
            .andExpect(content().string(expectedMessage))
    }
}
