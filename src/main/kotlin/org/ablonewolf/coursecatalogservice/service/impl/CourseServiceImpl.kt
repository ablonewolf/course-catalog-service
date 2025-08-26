package org.ablonewolf.coursecatalogservice.service.impl

import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.entity.Course
import org.ablonewolf.coursecatalogservice.repository.CourseRepository
import org.ablonewolf.coursecatalogservice.service.CourseService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository) : CourseService {

    companion object {
        private val log = LoggerFactory.getLogger(CourseServiceImpl::class.java)
    }

    override fun createNewCourse(courseCreateDTO: CourseCreateDTO): CourseResponseDTO {
        val course = courseCreateDTO.let {
            Course(
                name = it.name,
                category = it.category,
                description = it.description)
        }

        courseRepository.save(course)
        log.info("Created new course with name ${course.name}, its id is: ${course.id}")

        return course.let {
            object : CourseResponseDTO {
                override val id: Int? = it.id
                override val name: String? = it.name
                override val category: String? = it.category
                override val description: String? = it.description
            }
        }
    }
}
