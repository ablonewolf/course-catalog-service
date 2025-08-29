package org.ablonewolf.coursecatalogservice.advice

import org.ablonewolf.coursecatalogservice.exceptions.NotFoundException
import org.ablonewolf.coursecatalogservice.model.dto.response.ErrorResponseDTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

	companion object {
		private val log = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
	}

	override fun handleMethodArgumentNotValid(
		ex: MethodArgumentNotValidException,
		headers: HttpHeaders,
		status: HttpStatusCode,
		request: WebRequest
	): ResponseEntity<in Any>? {
		log.error("MethodArgumentNotValidException occurred, details: ${ex.message}", ex)
		val errors = mutableMapOf<String, String>()
		ex.bindingResult.allErrors.forEach { error ->
			val fieldName = (error as FieldError).field
			val errorMessage = error.defaultMessage ?: "Invalid value"
			errors[fieldName] = errorMessage
		}

		val response = ErrorResponseDTO(
			status = HttpStatus.BAD_REQUEST.value(),
			message = "Validation Failed",
			errors = errors
		)

		return ResponseEntity.badRequest().body(response)
	}

	@ExceptionHandler(NotFoundException::class)
	fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponseDTO> {
		log.error("NotFoundException occurred, details: ${ex.message}")
		val response = ErrorResponseDTO(
			status = HttpStatus.NOT_FOUND.value(),
			message = ex.message ?: "Resource not found"
		)
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
	}
}