package org.ablonewolf.coursecatalogservice.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "courses")
final class Course(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Int? = null,

	@Column(name = "name", nullable = false, length = 64)
	var name: String,

	@Column(name = "description", nullable = false, columnDefinition = "text")
	var description: String,

	@Column(name = "category", nullable = false, length = 32)

	var category: String,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructors_id", nullable = false)
	var instructor: Instructor? = null
) : BaseEntity()
