package org.ablonewolf.coursecatalogservice.controller

import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.service.CourseService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService
) {
    companion object {
        private val log = LoggerFactory.getLogger(CourseController::class.java)
    }

    @PostMapping
    fun createCourse(@RequestBody courseCreateDTO: CourseCreateDTO): ResponseEntity<CourseResponseDTO> {
        log.info("Creating new course with name ${courseCreateDTO.name}")
        val createdCourse = courseService.createNewCourse(courseCreateDTO)
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(createdCourse)
    }
}
