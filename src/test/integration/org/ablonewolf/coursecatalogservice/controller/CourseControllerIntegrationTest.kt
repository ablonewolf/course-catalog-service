package org.ablonewolf.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseUpdateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.PageData
import org.ablonewolf.coursecatalogservice.model.entity.Instructor
import org.ablonewolf.coursecatalogservice.repository.CourseRepository
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
class CourseControllerIntegrationTest : PostgresContainerInitializer() {

	@Autowired
	lateinit var webTestClient: WebTestClient

	@Autowired
	lateinit var courseRepository: CourseRepository

	@Autowired
	lateinit var instructorRepository: InstructorRepository

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	companion object {
		private lateinit var name: String
		private lateinit var category: String
		private lateinit var description: String
		private lateinit var courseResponseDTO: CourseResponseDTO
		private lateinit var courseCreateDTO: CourseCreateDTO

		private lateinit var instructorProgramming: Instructor
		private lateinit var instructorFramework: Instructor
		private lateinit var instructorFrontend: Instructor
		private lateinit var instructorDatabase: Instructor
		private lateinit var instructorDevOps: Instructor

		private lateinit var instructors: List<Instructor>


		data class CourseResponse(
			override val id: Int,
			override val name: String,
			override val category: String,
			override val description: String,
			override val instructorId: Int?,
			override val instructorName: String?
		) : CourseResponseDTO

		@JvmStatic
		@BeforeAll
		fun setup() {
			name = "Kotlin Programming"
			category = "Programming"
			description = "Learn the nuts and bolts of Programming with Kotlin"

			instructorProgramming = Instructor(
				name = "Arif Rahman",
				email = "arif.rahman+prog@ablonewolf.dev",
				bio = "Senior engineer specializing in Kotlin, Java and Python; focuses on language fundamentals, OOP, and clean code.",
				domain = "Programming"
			)

			instructorFramework = Instructor(
				name = "Sophia Tanaka",
				email = "sophia.tanaka+framework@ablonewolf.dev",
				bio = "Full-stack dev building enterprise backends with Spring Boot and Node/Express; emphasizes REST, testing, and scalability.",
				domain = "Framework"
			)

			instructorFrontend = Instructor(
				name = "Lucas Meyer",
				email = "lucas.meyer+frontend@ablonewolf.dev",
				bio = "Frontend architect focused on React and Angular; contributor to UI libraries and state management patterns.",
				domain = "Frontend"
			)

			instructorDatabase = Instructor(
				name = "Dr. Ayesha Karim",
				email = "ayesha.karim+db@ablonewolf.dev",
				bio = "Database consultant with experience in MySQL and MongoDB; query optimization, schema design, and data modeling.",
				domain = "Database"
			)

			instructorDevOps = Instructor(
				name = "Miguel Santos",
				email = "miguel.santos+devops@ablonewolf.dev",
				bio = "Cloud-native DevOps engineer; Docker, Kubernetes, CI/CD pipelines, and container orchestration in production.",
				domain = "DevOps"
			)

			instructors = listOf(
				instructorProgramming,
				instructorFramework,
				instructorFrontend,
				instructorDatabase,
				instructorDevOps
			)



			courseResponseDTO = object : CourseResponseDTO {
				override val id = 1
				override val name = this@Companion.name
				override val category = this@Companion.category
				override val description = this@Companion.description
				override val instructorId: Int? = 1
				override val instructorName: String? = instructorProgramming.name
			}

			courseCreateDTO = CourseCreateDTO(
				name = "Kotlin Programming",
				category = "Programming",
				description = "Learn the nuts and bolts of Programming with Kotlin",
				instructorId = instructors.first { it.domain == "Programming" }.id ?: 1,
			)
		}
	}

	@BeforeEach
	fun cleanUpDatabase() {
		instructors = this.instructorRepository.saveAll(instructors)
		this.courseRepository.deleteAll()
		courseCreateDTO = CourseCreateDTO(
			name = "Kotlin Programming",
			category = "Programming",
			description = "Learn the nuts and bolts of Programming with Kotlin",
			instructorId = instructors.first { it.domain == "Programming" }.id ?: 1,
		)
	}

	@Test
	@Order(1)
	fun test_courseCreateSuccess_WhenValidCreateDTOProvided_Returns201AndCourseDetails() {
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
	@Order(2)
	fun test_batchCreateSuccess_WhenValidCreateDTOListProvided_ReturnsHTTPStatus201() {
		// Arrange
		val courseCreateDTOs = listOf<CourseCreateDTO>(
			CourseCreateDTO(
				name = "Java Basics",
				category = "Programming",
				description = "Learn Java fundamentals and core concepts",
				instructorId = instructors.first { it.domain == "Programming" }.id
			),
			CourseCreateDTO(
				name = "Spring Boot",
				category = "Framework",
				description = "Build enterprise applications with Spring Boot",
				instructorId = instructors.first { it.domain == "Framework" }.id
			),
			CourseCreateDTO(
				name = "React Fundamentals",
				category = "Frontend",
				description = "Master React components and state management",
				instructorId = instructors.first { it.domain == "Frontend" }.id
			),
			CourseCreateDTO(
				name = "Docker Essentials",
				category = "DevOps",
				description = "Containerize applications with Docker",
				instructorId = instructors.first { it.domain == "DevOps" }.id
			),
			CourseCreateDTO(
				name = "MySQL Database",
				category = "Database",
				description = "Database design and SQL queries with MySQL",
				instructorId = instructors.first { it.domain == "Database" }.id
			)
		)

		// Act & Assert
		webTestClient.post()
			.uri("/courses/batch")
			.bodyValue(courseCreateDTOs)
			.exchange()
			.expectStatus().isCreated
	}


	@Test
	@Order(3)
	fun test_getCourseByIdSuccess_WhenValidIdProvided_Returns200AndCourseDetails() {
		// Arrange
		val createResponse = webTestClient.post()
			.uri("/courses")
			.bodyValue(courseCreateDTO)
			.exchange()
			.expectStatus().isCreated
			.expectBody()
			.returnResult()

		val createdCourse = objectMapper.readValue(
			createResponse.responseBody,
			CourseResponse::class.java
		)

		// Act
		val apiResult = webTestClient.get()
			.uri("/courses/${createdCourse.id}")
			.exchange()
			.expectStatus().isOk
			.expectBody()
			.returnResult()

		val fetchedCourse = objectMapper.readValue(
			apiResult.responseBody,
			CourseResponse::class.java
		)

		// Assert
		Assertions.assertEquals(createdCourse.id, fetchedCourse.id)
		Assertions.assertEquals(createdCourse.name, fetchedCourse.name)
		Assertions.assertEquals(createdCourse.category, fetchedCourse.category)
		Assertions.assertEquals(createdCourse.description, fetchedCourse.description)
		Assertions.assertEquals(createdCourse.instructorId, fetchedCourse.instructorId)
		Assertions.assertEquals(createdCourse.instructorName, fetchedCourse.instructorName)
	}

	@Test
	@Order(4)
	fun test_getAllCoursesSuccess_WhenValidFiltersProvided_Returns200AndPaginatedCourses() {
		// Arrange
		val courseCreateDTOs = listOf<CourseCreateDTO>(
			CourseCreateDTO(
				name = "Java Basics",
				category = "Programming",
				description = "Learn Java fundamentals and core concepts",
				instructorId = instructors.first { it.domain == "Programming" }.id
			),
			CourseCreateDTO(
				name = "Spring Boot",
				category = "Framework",
				description = "Build enterprise applications with Spring Boot",
				instructorId = instructors.first { it.domain == "Framework" }.id
			),
			CourseCreateDTO(
				name = "React Fundamentals",
				category = "Frontend",
				description = "Master React components and state management",
				instructorId = instructors.first { it.domain == "Frontend" }.id
			),
			CourseCreateDTO(
				name = "Docker Essentials",
				category = "DevOps",
				description = "Containerize applications with Docker",
				instructorId = instructors.first { it.domain == "DevOps" }.id
			),
			CourseCreateDTO(
				name = "MySQL Database",
				category = "Database",
				description = "Database design and SQL queries with MySQL",
				instructorId = instructors.first { it.domain == "Database" }.id
			),
			CourseCreateDTO(
				name = "Python Programming",
				category = "Programming",
				description = "Master Python syntax and object-oriented programming",
				instructorId = instructors.first { it.domain == "Programming" }.id
			),
			CourseCreateDTO(
				name = "Angular Framework",
				category = "Frontend",
				description = "Build dynamic web applications with Angular",
				instructorId = instructors.first { it.domain == "Frontend" }.id
			),
			CourseCreateDTO(
				name = "MongoDB Basics",
				category = "Database",
				description = "NoSQL database design and document operations",
				instructorId = instructors.first { it.domain == "Database" }.id
			),
			CourseCreateDTO(
				name = "Kubernetes Orchestration",
				category = "DevOps",
				description = "Container orchestration and cluster management",
				instructorId = instructors.first { it.domain == "DevOps" }.id
			),
			CourseCreateDTO(
				name = "Express.js API",
				category = "Framework",
				description = "Build RESTful APIs with Node.js and Express",
				instructorId = instructors.first { it.domain == "Framework" }.id
			)
		)

		// save the items in the database
		webTestClient.post()
			.uri("/courses/batch")
			.bodyValue(courseCreateDTOs)
			.exchange()
			.expectStatus().isCreated

		// Act
		val apiResult = webTestClient.get()
			.uri("/courses?page=1&size=10")
			.exchange()
			.expectStatus().isOk
			.expectBody()
			.returnResult()

		val pageData: PageData<CourseResponse> = objectMapper.readValue(
			apiResult.responseBody,
			objectMapper.typeFactory.constructParametricType(
				PageData::class.java,
				CourseResponse::class.java
			)
		)
		val courses = pageData.contents

		// Assert
		Assertions.assertEquals(10, courses.size)
		Assertions.assertEquals(courseCreateDTOs[0].name, courses[0].name)
		Assertions.assertEquals(courseCreateDTOs[0].category, courses[0].category)
		Assertions.assertEquals(courseCreateDTOs[0].description, courses[0].description)
		Assertions.assertEquals(courseCreateDTOs[0].instructorId, courses[0].instructorId)
	}

	@Test
	@Order(5)
	fun test_courseUpdateSuccess_WhenValidIdAndUpdateDTOProvided_Returns200AndUpdatedCourse() {
		// Arrange
		val updatedName = "Kotlin Masterclass"
		val updatedCategory = "Programming Language"
		val updatedDescription = "Learn Kotlin from the very scratch and become a Kotlin expert with hands-on projects"

		val createResponse = webTestClient.post()
			.uri("/courses")
			.bodyValue(courseCreateDTO)
			.exchange()
			.expectStatus().isCreated
			.expectBody()
			.returnResult()

		val createdCourse = objectMapper.readValue(
			createResponse.responseBody,
			CourseResponse::class.java
		)
		val courseUpdateDTO = CourseUpdateDTO(
			name = updatedName,
			category = updatedCategory,
			description = updatedDescription
		)

		// Act
		val apiResult = webTestClient.put()
			.uri("/courses/${createdCourse.id}")
			.bodyValue(courseUpdateDTO)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.returnResult()

		val updatedCourse = objectMapper.readValue(
			apiResult.responseBody,
			CourseResponse::class.java
		)

		// Assert
		Assertions.assertEquals(createdCourse.id, updatedCourse.id)
		Assertions.assertEquals(updatedName, updatedCourse.name)
		Assertions.assertEquals(updatedCategory, updatedCourse.category)
		Assertions.assertEquals(updatedDescription, updatedCourse.description)
	}

}
