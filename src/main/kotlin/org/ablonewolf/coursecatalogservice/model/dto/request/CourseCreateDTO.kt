package org.ablonewolf.coursecatalogservice.model.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CourseCreateDTO(
    @field:NotBlank(message = "Name cannot be empty")
    @field:Size(max = 64, message = "Name must be between 9 and 64 characters")
    val name: String,

    @field:NotBlank(message = "Category cannot be empty")
    @field:Size(max = 32, message = "Max length for category is 32 characters")
    val category: String,

    @field:NotBlank(message = "Description cannot be empty")
    val description: String,

    @field:NotNull(message = "Instructor ID cannot be null")
    val instructorId: Int?
)