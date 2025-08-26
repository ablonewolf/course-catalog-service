package org.ablonewolf.coursecatalogservice.repository

import org.ablonewolf.coursecatalogservice.model.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Int> {
}
