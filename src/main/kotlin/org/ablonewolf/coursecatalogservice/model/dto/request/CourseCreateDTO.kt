package org.ablonewolf.coursecatalogservice.model.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class CourseCreateDTO(
    @Min(1)
    @Max(64)
    val name: String,

    @Min(1)
    val category: String,

    @Min(1)
    @Max(32)
    val description: String
)
