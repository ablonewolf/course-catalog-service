package org.ablonewolf.coursecatalogservice.controller

import org.ablonewolf.coursecatalogservice.service.GreetingsService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greetings")
class GreetingController(
    private val greetingsService: GreetingsService
) {
    companion object {
        private val log = LoggerFactory.getLogger(GreetingController::class.java)
    }

    @GetMapping
    fun retrieveGreeting(@RequestParam("name") name: String): String {
        log.info("Greeting API called with name: {}", name)
        return greetingsService.retrieveGreeting(name)
    }
}
