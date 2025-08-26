package org.ablonewolf.coursecatalogservice.model.dto.request

import jakarta.validation.constraints.Min

data class CourseSearchDTO(
    @Min(1)
    val pageNumber: Int,

    @Min(1)
    val pageSize: Int,

    val name: String?,
    val category: String?
)
