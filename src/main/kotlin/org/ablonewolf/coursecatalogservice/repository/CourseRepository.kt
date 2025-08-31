package org.ablonewolf.coursecatalogservice.repository

import org.ablonewolf.coursecatalogservice.model.dto.request.CourseSearchDTO
import org.ablonewolf.coursecatalogservice.model.dto.response.CourseResponseDTO
import org.ablonewolf.coursecatalogservice.model.entity.Course
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Int> {

	@Query(
		value = """
    SELECT 
        course.id AS id,
        course.name AS name,
        course.category AS category,
        course.description AS description,
		instructor.id AS instructorId,
		instructor.name AS instructorName
    FROM Course course INNER JOIN Instructor instructor ON course.instructor.id = instructor.id
    WHERE
        (:#{#searchDTO.name} IS NULL OR LOWER(course.name) LIKE CONCAT('%', LOWER(:#{#searchDTO.name}), '%'))
        AND (:#{#searchDTO.category} IS NULL OR 
        LOWER(course.category) LIKE CONCAT('%', LOWER(:#{#searchDTO.category}) , '%'))"""
	)
	fun getAllCourses(
		pageable: Pageable, @Param("searchDTO") courseSearchDTO: CourseSearchDTO
	): Page<CourseResponseDTO>

	@Query(
		value = """
    SELECT 
        course.id AS id, 
        course.name AS name, 
        course.category AS category, 
        course.description AS description,
		instructor.id AS instructorId,
		instructor.name AS instructorName
    FROM Course course INNER JOIN Instructor instructor ON course.instructor.id = instructor.id
    WHERE
        course.id = :id"""
	)
	fun getCourseById(@Param("id") id: Int): CourseResponseDTO
}
