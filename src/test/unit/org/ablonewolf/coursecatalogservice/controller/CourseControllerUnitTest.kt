package org.ablonewolf.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.ablonewolf.coursecatalogservice.exceptions.NotFoundException
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseUpdateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.PageData
import org.ablonewolf.coursecatalogservice.service.CourseService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CourseController::class])
class CourseControllerUnitTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@MockitoBean
	private lateinit var courseService: CourseService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	private lateinit var name: String
	private lateinit var category: String
	private lateinit var description: String
	private lateinit var courseResponseDTO: CourseResponseDTO
	private val courseCreateDTOs = mutableListOf<CourseCreateDTO>()

	@BeforeEach
	fun setup() {
		name = "Kotlin Programming"
		category = "Programming"
		description = "Learn the nuts and bolts of Programming with Kotlin"

		courseResponseDTO = object : CourseResponseDTO {
			override val id = 1
			override val name = this@CourseControllerUnitTest.name
			override val category = this@CourseControllerUnitTest.category
			override val description = this@CourseControllerUnitTest.description
		}

		courseCreateDTOs.addAll(
			listOf(
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
		)
	}

	@Test
	fun test_courseCreateSuccess_WhenValidCreateDTOProvided_Returns201AndCourseDetails() {
		// Arrange
		val courseCreateDTO = CourseCreateDTO(
			name = name,
			category = category,
			description = description
		)

		whenever(courseService.createNewCourse(courseCreateDTO))
			.thenReturn(courseResponseDTO)

		// Act & Assert
		mockMvc.perform(
			post("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(courseCreateDTO))
		)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.category").value(category))
			.andExpect(jsonPath("$.description").value(description))
	}

	@Test
	fun test_getAllCoursesSuccess_WhenValidFiltersProvided_Returns200AndPaginatedCourses() {
		// Arrange
		val pageData = PageData(
			contents = listOf(courseResponseDTO),
			totalElements = 1,
			totalPages = 1,
			currentPage = 1
		)

		whenever(courseService.getAllCourses(any()))
			.thenReturn(pageData)

		// Act & Assert
		mockMvc.perform(
			get("/courses")
				.param("page", "1")
				.param("size", "10")
				.param("name", "Kotlin")
				.param("category", "Programming")
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.contents[0].id").value(1))
			.andExpect(jsonPath("$.contents[0].name").value("Kotlin Programming"))
			.andExpect(jsonPath("$.totalElements").value(1))
	}

	@Test
	fun test_courseUpdateSuccess_WhenValidIdAndUpdateDTOProvided_Returns200AndUpdatedCourse() {
		// Arrange
		val courseUpdateDTO = CourseUpdateDTO(
			name = "Advanced Kotlin",
			category = "Programming",
			description = null
		)

		val updatedCourseResponseDTO = object : CourseResponseDTO {
			override val id = 1
			override val name = "Advanced Kotlin"
			override val category = "Programming"
			override val description = "Learn the nuts and bolts of Programming with Kotlin"
		}

		whenever(courseService.updateCourse(any(), any()))
			.thenReturn(updatedCourseResponseDTO)

		// Act & Assert
		mockMvc.perform(
			put("/courses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(courseUpdateDTO))
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("Advanced Kotlin"))
			.andExpect(jsonPath("$.category").value("Programming"))
			.andExpect(
				jsonPath("$.description")
					.value("Learn the nuts and bolts of Programming with Kotlin")
			)
	}

	@Test
	fun test_batchCreateSuccess_WhenValidCreateDTOListProvided_ReturnsHTTPStatus201() {
		// Arrange
		doNothing().whenever(courseService).createMultipleCourses(courseCreateDTOs)

		// Act & Assert
		mockMvc.perform(
			post("/courses/batch")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(courseCreateDTOs))
		)
			.andExpect(status().isCreated)
	}

	@Test
	fun test_getCourseByIdSuccess_WhenValidIdProvided_Returns200AndCourseDetails() {
		// Arrange
		whenever(courseService.getCourseById(1))
			.thenReturn(courseResponseDTO)

		// Act & Assert
		mockMvc.perform(get("/courses/1"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.category").value(category))
			.andExpect(jsonPath("$.description").value(description))
	}

	@Test
	fun test_deleteCourseSuccess_WhenValidIdProvided_Returns204() {
		// Arrange
		doNothing().whenever(courseService).deleteCourse(1)

		// Act & Assert
		mockMvc.perform(delete("/courses/1"))
			.andExpect(status().isNoContent)
	}

	@Test
	fun test_createCourseFailure_WhenEmptyNameProvided_ReturnsBadRequest() {
		// Arrange
		val invalidCourseCreateDTO = CourseCreateDTO(
			name = "",  // Invalid: name is blank
			category = category,
			description = description
		)

		// Act & Assert
		mockMvc.perform(
			post("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidCourseCreateDTO))
		)
			.andExpect(status().isBadRequest)
			.andExpect(jsonPath("$.errors").exists())
			.andExpect(
				jsonPath("$.errors['name']")
					.value("Name cannot be empty")
			)
	}

	@Test
	fun test_createCourseFailure_WhenEmptyCategoryProvided_ReturnsBadRequest() {
		// Arrange
		val invalidCourseCreateDTO = CourseCreateDTO(
			name = name,
			category = "",  // Invalid: category is blank
			description = description
		)

		// Act & Assert
		mockMvc.perform(
			post("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidCourseCreateDTO))
		)
			.andExpect(status().isBadRequest)
			.andExpect(jsonPath("$.errors").exists())
			.andExpect(
				jsonPath("$.errors['category']")
					.value("Category cannot be empty")
			)
	}

	@Test
	fun test_createCourseFailure_WhenEmptyDescriptionProvided_ReturnsBadRequest() {
		// Arrange
		val invalidCourseCreateDTO = CourseCreateDTO(
			name = name,
			category = category,
			description = "" // Invalid: description is blank
		)

		// Act & Assert
		mockMvc.perform(
			post("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidCourseCreateDTO))
		)
			.andExpect(status().isBadRequest)
			.andExpect(jsonPath("$.errors").exists())
			.andExpect(
				jsonPath("$.errors['description']")
					.value("Description cannot be empty")
			)
	}

	@Test
	fun testGetCourseByIdFailure_WhenNonExistentIdProvided_ReturnsNotFound() {
		// Arrange
		whenever(courseService.getCourseById(999))
			.thenThrow(NotFoundException("Course with id 999 not found"))

		// Act & Assert
		mockMvc.perform(get("/courses/999"))
			.andExpect(status().isNotFound)
			.andExpect(
				jsonPath("$.message")
					.value("Course with id 999 not found")
			)
	}

	@Test
	fun testUpdateCourseByIdFailure_WhenNonExistentIdProvided_ReturnsNotFound() {
		// Arrange
		val nonExistentId = 999
		val courseUpdateDTO = CourseUpdateDTO(
			name = "Advanced Kotlin",
			category = "Programming",
			description = null
		)

		whenever(courseService.updateCourse(nonExistentId, courseUpdateDTO))
			.thenThrow(NotFoundException("Course with id 999 not found"))

		// Act & Assert
		mockMvc.perform(
			put("/courses/$nonExistentId")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(courseUpdateDTO))
		)
			.andExpect(status().isNotFound)
			.andExpect(
				jsonPath("$.message")
					.value("Course with id 999 not found")
			)
	}
}