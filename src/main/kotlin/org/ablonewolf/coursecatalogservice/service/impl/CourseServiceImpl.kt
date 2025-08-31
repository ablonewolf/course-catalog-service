package org.ablonewolf.coursecatalogservice.service.impl

import org.ablonewolf.coursecatalogservice.exceptions.NotFoundException
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseSearchDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseUpdateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.PageData
import org.ablonewolf.coursecatalogservice.model.entity.Course
import org.ablonewolf.coursecatalogservice.repository.CourseRepository
import org.ablonewolf.coursecatalogservice.service.CourseService
import org.ablonewolf.coursecatalogservice.service.InstructorService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class CourseServiceImpl(
	private val courseRepository: CourseRepository,
	private val instructorService: InstructorService
) : CourseService {

	companion object {
		private val log = LoggerFactory.getLogger(CourseServiceImpl::class.java)
	}

	override fun createNewCourse(courseCreateDTO: CourseCreateDTO): CourseResponseDTO {
		val instructor = instructorService.findInstructorById(courseCreateDTO.instructorId ?: 0)
		val course = courseCreateDTO.let {
			Course(
				name = it.name,
				category = it.category,
				description = it.description,
				instructor = instructor
			)
		}

		courseRepository.save(course)
		log.info("Created new course with name ${course.name}, its id is: ${course.id}")

		return getCourseResponseFromEntity(course)
	}


	override fun getAllCourses(courseSearchDTO: CourseSearchDTO): PageData<CourseResponseDTO> {
		val pageable = PageRequest.of(courseSearchDTO.pageNumber - 1, courseSearchDTO.pageSize)
		val coursePage = courseRepository.getAllCourses(pageable, courseSearchDTO)
		return PageData(
			contents = coursePage.content,
			currentPage = coursePage.number + 1,
			totalElements = coursePage.totalElements,
			totalPages = coursePage.totalPages
		)
	}

	override fun updateCourse(id: Int, courseUpdateDTO: CourseUpdateDTO): CourseResponseDTO {
		val existingCourse = courseRepository.findById(id)
			.orElseThrow { NotFoundException("Course with id $id not found") }

		existingCourse?.let { course ->
			course.name = courseUpdateDTO.name ?: course.name
			course.category = courseUpdateDTO.category ?: course.category
			course.description = courseUpdateDTO.description ?: course.description
		}
		courseRepository.save(existingCourse)
		log.info("Updated course with id: $id")

		return getCourseResponseFromEntity(existingCourse)
	}

	override fun createMultipleCourses(courseCreateDTOs: List<CourseCreateDTO>) {
		val createdCourses = mutableListOf<Course>()
		val instructorIDs = courseCreateDTOs.map { it.instructorId }
		val instructorMap = instructorService.findMultipleInstructorsByIds(instructorIDs.filterNotNull())

		courseCreateDTOs.forEach {
			val course = Course(
				name = it.name,
				category = it.category,
				description = it.description,
				instructor = instructorMap[it.instructorId]
			)
			createdCourses.add(course)
		}

		courseRepository.saveAll(createdCourses)
		log.info("Created ${createdCourses.size} new courses")
	}

	override fun getCourseById(id: Int): CourseResponseDTO {
		if (!courseRepository.existsById(id)) {
			throw NotFoundException("Course with id $id not found")
		}
		return courseRepository.getCourseById(id)
	}

	override fun deleteCourse(id: Int) {
		if (!courseRepository.existsById(id)) {
			throw NotFoundException("Course with id $id not found")
		}
		courseRepository.deleteById(id)
		log.info("Deleted course with id: $id")
	}

	private fun getCourseResponseFromEntity(course: Course): CourseResponseDTO = course.let {
		object : CourseResponseDTO {
			override val id: Int? = it.id
			override val name: String? = it.name
			override val category: String? = it.category
			override val description: String? = it.description
			override val instructorId: Int? = it.instructor?.id
			override val instructorName: String? = it.instructor?.name
		}
	}
}

