package org.ablonewolf.coursecatalogservice.controller

import io.swagger.v3.oas.annotations.Operation
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseSearchDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseUpdateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.PageData
import org.ablonewolf.coursecatalogservice.service.CourseService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
	@Operation(summary = "Add a new course")
	fun createCourse(@RequestBody courseCreateDTO: CourseCreateDTO): ResponseEntity<CourseResponseDTO> {
		log.info("Creating new course with name ${courseCreateDTO.name}")
		val createdCourse = courseService.createNewCourse(courseCreateDTO)
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(createdCourse)
	}

	@PostMapping("/get-all")
	@Operation(summary = "Get all courses, with pagination and optional filtering by name and category")
	fun getAllCourses(@RequestBody courseSearchDTO: CourseSearchDTO): ResponseEntity<PageData<CourseResponseDTO>> {
		log.info("Fetching all course existing courses")
		val courses = courseService.getAllCourses(courseSearchDTO)
		return ResponseEntity.status(HttpStatus.OK.value()).body(courses)
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update an existing course by its id")
	fun updateCourse(
		@PathVariable id: Int,
		@RequestBody courseUpdateDTO: CourseUpdateDTO
	): ResponseEntity<CourseResponseDTO> {
		log.info("Updating course with id: $id")
		val updatedCourse = courseService.updateCourse(id, courseUpdateDTO)
		return ResponseEntity.ok()
			.body(updatedCourse)
	}

	@PostMapping("/batch")
	@Operation(summary = "Create multiple new courses in a single request")
	fun createMultipleCourses(
		@RequestBody courseCreateDTOs: List<CourseCreateDTO>): ResponseEntity<Unit> {
		log.info("Creating multiple new courses, count: ${courseCreateDTOs.size}")
		courseService.createMultipleCourses(courseCreateDTOs)
		return ResponseEntity.status(HttpStatus.CREATED.value()).build<Unit>()
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a course by its id")
	fun deleteCourse(@PathVariable id: Int): ResponseEntity<Unit> {
		log.info("Deleting course with id: $id")
		courseService.deleteCourse(id)
		return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build<Unit>()
	}
}
