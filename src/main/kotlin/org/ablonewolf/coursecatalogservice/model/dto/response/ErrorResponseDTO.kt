package org.ablonewolf.coursecatalogservice.model.dto.response

data class ErrorResponseDTO(
	val status: Int,
	val message: String,
	val errors: MutableMap<String, String> = mutableMapOf<String, String>()
)
