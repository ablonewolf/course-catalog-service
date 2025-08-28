package org.ablonewolf.coursecatalogservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
	lateinit var mockMvc: MockMvc

	@MockitoBean
	lateinit var courseService: CourseService

	@Autowired
	lateinit var objectMapper: ObjectMapper

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
	fun createCourse() {
		val courseCreateDTO = CourseCreateDTO(
			name = name,
			category = category,
			description = description
		)

		whenever(courseService.createNewCourse(courseCreateDTO))
			.thenReturn(courseResponseDTO)

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
	fun getAllCourses() {
		val pageData = PageData(
			contents = listOf(courseResponseDTO),
			totalElements = 1,
			totalPages = 1,
			currentPage = 1
		)

		whenever(courseService.getAllCourses(any()))
			.thenReturn(pageData)

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
	fun updateCourse() {
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

		mockMvc.perform(
			put("/courses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(courseUpdateDTO))
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("Advanced Kotlin"))
			.andExpect(jsonPath("$.category").value("Programming"))
			.andExpect(jsonPath("$.description").value("Learn the nuts and bolts of Programming with Kotlin"))
	}

	@Test
	fun createMultipleCourses() {
		doNothing().whenever(courseService).createMultipleCourses(courseCreateDTOs)

		mockMvc.perform(
			post("/courses/batch")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(courseCreateDTOs))
		)
			.andExpect(status().isCreated)
	}

	@Test
	fun getCourseById() {
		whenever(courseService.getCourseById(1))
			.thenReturn(courseResponseDTO)

		mockMvc.perform(get("/courses/1"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.category").value(category))
			.andExpect(jsonPath("$.description").value(description))
	}

	@Test
	fun deleteCourse() {
		doNothing().whenever(courseService).deleteCourse(1)

		mockMvc.perform(delete("/courses/1"))
			.andExpect(status().isNoContent)
	}
}