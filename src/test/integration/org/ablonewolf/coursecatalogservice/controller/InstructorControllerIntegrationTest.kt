package org.ablonewolf.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorResponseDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.ErrorResponseDTO
import org.ablonewolf.coursecatalogservice.repository.InstructorRepository
import org.ablonewolf.coursecatalogservice.util.PostgresContainerInitializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class InstructorControllerIntegrationTest : PostgresContainerInitializer() {

	@Autowired
	lateinit var webTestClient: WebTestClient

	@Autowired
	lateinit var instructorRepository: InstructorRepository

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	companion object {
		private lateinit var name: String
		private lateinit var email: String
		private lateinit var bio: String
		private lateinit var domain: String
		private lateinit var instructorCreateDto: InstructorCreateDTO
		private lateinit var instructorResponse: InstructorResponseDTO


		data class InstructorResponse(
			override val id: Int?,
			override val name: String?,
			override val email: String?,
			override val domain: String?,
			override val bio: String?
		) : InstructorResponseDTO

		@JvmStatic
		@BeforeAll
		fun setup() {
			// Arranging test data
			name = "Dr. Jane Smith"
			email = "jane.smith@example.com"
			bio = "Expert in computer science and software engineering."
			domain = "Computer Science"

			instructorCreateDto = InstructorCreateDTO(
				name = this@Companion.name,
				email = this@Companion.email,
				bio = this@Companion.bio,
				domain = this@Companion.domain
			)

			instructorResponse = InstructorResponse(
				id = 1,
				name = this@Companion.name,
				email = this@Companion.email,
				domain = this@Companion.domain,
				bio = this@Companion.bio
			)
		}
	}

	@BeforeEach
	fun cleanUpDatabase() {
		this.instructorRepository.deleteAll()
	}

	@Test
	@Order(1)
	fun test_instructorCreateSuccess_WhenValidCreateDTOProvided_Returns201AndInstructorDetails() {
		// Act
		val apiResult = webTestClient.post()
			.uri("/instructors")
			.bodyValue(instructorCreateDto)
			.exchange()
			.expectStatus().isCreated
			.expectBody()
			.returnResult()

		val createdInstructor = objectMapper.readValue(
			apiResult.responseBody,
			InstructorResponse::class.java
		)

		// Assert
		Assertions.assertEquals(instructorResponse.id, createdInstructor.id)
		Assertions.assertEquals(instructorResponse.name, createdInstructor.name)
		Assertions.assertEquals(instructorResponse.email, createdInstructor.email)
		Assertions.assertEquals(instructorResponse.domain, createdInstructor.domain)
		Assertions.assertEquals(instructorResponse.bio, createdInstructor.bio)
	}

	@Test
	@Order(2)
	fun test_instructorCreateFail_WhenInvalidEmailAddressProvided_Returns400AndErrorDetails() {
		// Arrange
		val invalidEmail = "invalid-email-format"
		val invalidInstructorCreateDto = InstructorCreateDTO(
			name = name,
			email = invalidEmail,
			bio = bio,
			domain = domain
		)

		val expectedErrorMessage = ErrorResponseDTO(
			status = 400,
			message = "Validation Failed",
			errors = mutableMapOf("email" to "Email should be a valid email address")
		)

		// Act
		val apiResult = webTestClient.post()
			.uri("/instructors")
			.bodyValue(invalidInstructorCreateDto)
			.exchange()
			.expectStatus().isBadRequest
			.expectBody()
			.returnResult()

		val errorResponse = objectMapper.readValue(apiResult.responseBody, ErrorResponseDTO::class.java)

		// Assert
		Assertions.assertEquals(expectedErrorMessage.status, errorResponse.status)
		Assertions.assertEquals(expectedErrorMessage.message, errorResponse.message)
		Assertions.assertTrue(errorResponse.errors.contains("email"))
		Assertions.assertEquals(expectedErrorMessage.errors["email"], errorResponse.errors["email"])
	}

	@Test
	fun test_createInstructorFailure_WhenEmptyNameProvided_ReturnsBadRequest() {
		// Arrange
		val invalidInstructorCreateDTO = InstructorCreateDTO(
			name = "",  // Invalid: name is blank
			email = email,
			bio = bio,
			domain = domain
		)

		val expectedErrorMessage = ErrorResponseDTO(
			status = 400,
			message = "Validation Failed",
			errors = mutableMapOf("name" to "Name cannot be empty")
		)

		// Act
		val apiResult = webTestClient.post()
			.uri("/instructors")
			.bodyValue(invalidInstructorCreateDTO)
			.exchange()
			.expectStatus().isBadRequest
			.expectBody()
			.returnResult()

		val errorResponse = objectMapper.readValue(apiResult.responseBody, ErrorResponseDTO::class.java)

		// Assert
		Assertions.assertEquals(expectedErrorMessage.status, errorResponse.status)
		Assertions.assertEquals(expectedErrorMessage.message, errorResponse.message)
		Assertions.assertTrue(errorResponse.errors.contains("name"))
		Assertions.assertEquals(expectedErrorMessage.errors["name"], errorResponse.errors["name"])
	}
}