package org.ablonewolf.coursecatalogservice.service

import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseSearchDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.CourseUpdateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.PageData

interface CourseService {

	fun createNewCourse(courseCreateDTO: CourseCreateDTO): CourseResponseDTO

	fun getAllCourses(courseSearchDTO: CourseSearchDTO): PageData<CourseResponseDTO>

	fun updateCourse(id: Int, courseUpdateDTO: CourseUpdateDTO): CourseResponseDTO

	fun createMultipleCourses(courseCreateDTOs: List<CourseCreateDTO>)

	fun deleteCourse(id: Int)
}
