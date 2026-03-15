package org.ablonewolf.coursecatalogservice.repository

import org.ablonewolf.coursecatalogservice.model.entity.Instructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface InstructorRepository : JpaRepository<Instructor, Int> {

	fun findInstructorsByIdIn(ids: List<Int>): List<Instructor>
	
	@Modifying
	@Transactional
	@Query(value = "ALTER SEQUENCE instructors_id_seq RESTART WITH 1", nativeQuery = true)
	fun resetIdSequence()
}