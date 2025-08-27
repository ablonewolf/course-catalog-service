package org.ablonewolf.coursecatalogservice.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "courses")
class Course(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Int? = null,

	@Column(name = "name", nullable = false, length = 64)
	var name: String,

	@Column(name = "description", nullable = false, columnDefinition = "text")
	var description: String,

	@Column(name = "category", nullable = false, length = 32)
	var category: String
) : BaseEntity()
