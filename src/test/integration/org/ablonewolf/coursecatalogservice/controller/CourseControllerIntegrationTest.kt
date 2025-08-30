package org.ablonewolf.coursecatalogservice.controller

import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.util.PostgresContainerInitializer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntegrationTest : PostgresContainerInitializer() {

	@Autowired
	lateinit var webTestClient: WebTestClient

	companion object {
		private lateinit var name: String
		private lateinit var category: String
		private lateinit var description: String
		private lateinit var courseResponseDTO: CourseResponseDTO

		@JvmStatic
		@BeforeAll
		fun setup() {
			name = "Kotlin Programming"
			category = "Programming"
			description = "Learn the nuts and bolts of Programming with Kotlin"

			courseResponseDTO = object : CourseResponseDTO {
				override val id = 1
				override val name = this@Companion.name
				override val category = this@Companion.category
				override val description = this@Companion.description
			}
		}
	}

	@Test
	fun test_courseCreateSuccess_WhenValidCreateDTOProvided_Returns201AndCourseDetails() {
		// Arrange
		val courseCreateDTO = CourseCreateDTO(
			"Kotlin Programming", "Programming",
			"Learn the nuts and bolts of Programming with Kotlin"
		)

		// Act & Assert
		webTestClient.post()
			.uri("/courses")
			.bodyValue(courseCreateDTO)
			.exchange()
			.expectStatus().isCreated
			.expectBody()
			.jsonPath("$.name").isEqualTo("Kotlin Programming")
			.jsonPath("$.category").isEqualTo("Programming")
			.jsonPath("$.description").isEqualTo("Learn the nuts and bolts of Programming with Kotlin")
			.jsonPath("$.id").isEqualTo(1)
	}

	@Test
	fun test_batchCreateSuccess_WhenValidCreateDTOListProvided_ReturnsHTTPStatus201() {
		// Arrange
		val courseCreateDTOs = listOf<CourseCreateDTO>(
			CourseCreateDTO(
				name = "Java Basics",
				category = "Programming",
				description = "Learn Java fundamentals and core concepts"
			),
			CourseCreateDTO(
				name = "Spring Boot",
				category = "Framework",
				description = "Build enterprise applications with Spring Boot"
			),
			CourseCreateDTO(
				name = "React Fundamentals",
				category = "Frontend",
				description = "Master React components and state management"
			),
			CourseCreateDTO(
				name = "Docker Essentials",
				category = "DevOps",
				description = "Containerize applications with Docker"
			),
			CourseCreateDTO(
				name = "MySQL Database",
				category = "Database",
				description = "Database design and SQL queries with MySQL"
			)
		)

		// Act & Assert
		webTestClient.post()
			.uri("/courses/batch")
			.bodyValue(courseCreateDTOs)
			.exchange()
			.expectStatus().isCreated
	}

}
