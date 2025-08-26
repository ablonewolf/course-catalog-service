package org.ablonewolf.coursecatalogservice.service

import org.ablonewolf.coursecatalogservice.model.dto.request.CourseCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO

interface CourseService {

    fun createNewCourse(courseCreateDTO: CourseCreateDTO) : CourseResponseDTO
}
