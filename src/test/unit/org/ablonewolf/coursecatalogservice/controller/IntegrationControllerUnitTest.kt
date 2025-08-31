package org.ablonewolf.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorResponseDTO
import org.ablonewolf.coursecatalogservice.service.InstructorService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [InstructorController::class])
class IntegrationControllerUnitTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@MockitoBean
	private lateinit var instructorService: InstructorService

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
		}
	}

	@Test()
	fun test_instructorCreateSuccess_WhenValidCreateDTOProvided_Returns201AndInstructorDetails() {
		// Arrange
		instructorCreateDto = InstructorCreateDTO(
			name = name,
			email = email,
			bio = bio,
			domain = domain
		)

		instructorResponse = InstructorResponse(
			id = 1,
			name = name,
			email = email,
			domain = domain,
			bio = bio
		)

		whenever(instructorService.createNewInstructor(instructorCreateDto))
			.thenReturn(instructorResponse)

		// Act & Assert
		mockMvc.perform(
			post("/instructors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(instructorCreateDto))
		)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.id").value(instructorResponse.id))
			.andExpect(jsonPath("$.name").value(instructorResponse.name))
			.andExpect(jsonPath("$.email").value(instructorResponse.email))
			.andExpect(jsonPath("$.domain").value(instructorResponse.domain))
			.andExpect(jsonPath("$.bio").value(instructorResponse.bio))

	}
}