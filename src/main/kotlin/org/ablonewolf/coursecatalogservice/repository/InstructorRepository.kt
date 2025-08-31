package org.ablonewolf.coursecatalogservice.repository

import org.ablonewolf.coursecatalogservice.model.entity.Instructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InstructorRepository : JpaRepository<Instructor, Int> {

	fun findInstructorsByIdIn(ids: List<Int>): List<Instructor>
}