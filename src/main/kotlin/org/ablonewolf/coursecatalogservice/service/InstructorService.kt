package org.ablonewolf.coursecatalogservice.service

import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorResponseDTO
import org.ablonewolf.coursecatalogservice.model.entity.Instructor

interface InstructorService {

	fun createNewInstructor(instructorCreateDTO: InstructorCreateDTO): InstructorResponseDTO

	fun findInstructorById(id: Int): Instructor
}