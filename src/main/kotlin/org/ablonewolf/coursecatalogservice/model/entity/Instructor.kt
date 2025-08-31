package org.ablonewolf.coursecatalogservice.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "instructors")
class Instructor(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Int? = null,

	@Column(name = "name", nullable = false, length = 64)
	var name: String,

	@Column(name = "email", nullable = false, unique = true, length = 128)
	var email: String,

	@Column(name = "bio", columnDefinition = "text")
	var bio: String? = null,

	@Column(name = "domain", nullable = false, length = 32)
	var domain: String
) : BaseEntity()