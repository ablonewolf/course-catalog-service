package org.ablonewolf.coursecatalogservice.model.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class InstructorCreateDTO(

	@field:NotBlank(message = "Name cannot be empty")
	@field:Size(max = 64, message = "Name must be between 9 and 64 characters")
	val name: String,

	@field:NotBlank(message = "Email cannot be empty")
	@field:Size(max = 128, message = "Email must be between 5 and 128 characters")
	@field:Email
	val email: String,

	val bio: String?,

	@field:NotBlank(message = "domain cannot be empty")
	@field:Size(max = 32, message = "Name of the domain must be within 32 characters")
	val domain: String
)
