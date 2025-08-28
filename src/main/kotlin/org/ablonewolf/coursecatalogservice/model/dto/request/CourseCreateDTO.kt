package org.ablonewolf.coursecatalogservice.model.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank

data class CourseCreateDTO(
    @NotBlank(message = "Name cannot be empty")
    @Max(64)
    val name: String,

    @NotBlank(message = "category cannot be empty")
    @Max(32, message = "Max length for category is 32 characters")
    val category: String,

    @NotBlank(message = "description cannot be empty")
    val description: String
)
