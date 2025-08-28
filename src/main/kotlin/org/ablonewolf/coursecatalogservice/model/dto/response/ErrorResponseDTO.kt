package org.ablonewolf.coursecatalogservice.model.dto.response

data class ErrorResponseDTO(
	val status: Int,
	val error: String,
	val errors: MutableMap<String, String> = mutableMapOf<String, String>()
)
