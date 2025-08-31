package org.ablonewolf.coursecatalogservice.controller

import io.swagger.v3.oas.annotations.Operation
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorResponseDTO
import org.ablonewolf.coursecatalogservice.service.InstructorService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/instructors")
class InstructorController(
	private val instructorService: InstructorService,
) {
	companion object {
		private val log = LoggerFactory.getLogger(InstructorController::class.java)
	}

	@PostMapping
	@Operation(summary = "Add a new instructor")
	fun createInstructor(@Validated @RequestBody instructorCreateDTO: InstructorCreateDTO):
			ResponseEntity<InstructorResponseDTO> {
		log.info("Creating new instructor with name ${instructorCreateDTO.name}")
		val createdInstructor = instructorService.createNewInstructor(instructorCreateDTO)
		return ResponseEntity.status(HttpStatus.CREATED.value())
			.body(createdInstructor)
	}
}