package org.ablonewolf.coursecatalogservice.service.impl

import org.ablonewolf.coursecatalogservice.service.GreetingsService
import org.springframework.stereotype.Service

@Service
class GreetingsServiceImpl : GreetingsService {

    override fun retrieveGreeting(name: String): String {
        return "Hello there ${name}, welcome to the Course Catalog Service!"
    }
}
