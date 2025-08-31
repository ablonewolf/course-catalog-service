package org.ablonewolf.coursecatalogservice.service.impl

import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorCreateDTO
import org.ablonewolf.coursecatalogservice.model.dto.request.InstructorResponseDTO
import org.ablonewolf.coursecatalogservice.model.entity.Instructor
import org.ablonewolf.coursecatalogservice.repository.InstructorRepository
import org.ablonewolf.coursecatalogservice.service.InstructorService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InstructorServiceImpl(private val instructorRepository: InstructorRepository) : InstructorService {

	companion object {
		private val log = LoggerFactory.getLogger(InstructorServiceImpl::class.java)
	}

	override fun createNewInstructor(instructorCreateDTO: InstructorCreateDTO): InstructorResponseDTO {
		var instructor = instructorCreateDTO.let {
			Instructor(name = it.name, email = it.email, domain = it.domain, bio = it.bio, courses = HashSet())
		}

		instructor = instructorRepository.save(instructor)
		log.info("Created new instructor with name ${instructor.name}, its id is: ${instructor.id}")

		return instructor.let {
			object : InstructorResponseDTO {
				override val id: Int? = it.id
				override val name: String? = it.name
				override val email: String? = it.email
				override val domain: String? = it.domain
				override val bio: String? = it.bio
			}
		}
	}
}