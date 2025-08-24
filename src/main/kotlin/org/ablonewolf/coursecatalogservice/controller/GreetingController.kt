package org.ablonewolf.coursecatalogservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greetings")
class GreetingController {

    @GetMapping()
    fun retrieveGreeting(@RequestParam("name") name: String): String {
        return "Hello there ${name}, welcome to the Course Catalog Service!"
    }
}
