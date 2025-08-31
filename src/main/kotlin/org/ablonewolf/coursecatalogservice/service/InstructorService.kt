package org.ablonewolf.coursecatalogservice.service

import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorResponseDTO

interface InstructorService {

	fun createNewInstructor(instructorCreateDTO: InstructorCreateDTO): InstructorResponseDTO
}