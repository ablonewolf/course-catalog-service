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
    val id: Int? = null,

    @Column(name = "name", nullable = false, length = 64)
    val name: String,

    @Column(name = "description", nullable = false, columnDefinition = "text")
    val description: String,

    @Column(name = "category", nullable = false, length = 32)
    val category: String
) : BaseEntity()
