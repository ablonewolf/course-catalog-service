package org.ablonewolf.coursecatalogservice.model.dto.request

import com.fasterxml.jackson.annotation.JsonIgnore

data class CourseUpdateDTO(
	@JsonIgnore
	val name: String?,
	val category: String?,
	val description: String?
)
