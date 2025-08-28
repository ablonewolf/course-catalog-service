package org.ablonewolf.coursecatalogservice.advice

import org.ablonewolf.coursecatalogservice.model.dto.response.ErrorResponseDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

	override fun handleMethodArgumentNotValid(
		ex: MethodArgumentNotValidException,
		headers: HttpHeaders,
		status: HttpStatusCode,
		request: WebRequest
	): ResponseEntity<in Any>? {
		val errors = mutableMapOf<String, String>()
		ex.bindingResult.allErrors.forEach { error ->
			val fieldName = (error as FieldError).field
			val errorMessage = error.defaultMessage ?: "Invalid value"
			errors[fieldName] = errorMessage
		}

		val response = ErrorResponseDTO(
			status = HttpStatus.BAD_REQUEST.value(),
			error = "Validation Failed",
			errors = errors
		)

		return ResponseEntity.badRequest().body(response)
	}
}