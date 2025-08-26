package org.ablonewolf.coursecatalogservice.model.dto.response

data class PageData<T>(
    val contents: List<T>,
    val totalPages: Int,
    val currentPage: Int,
    val totalElements: Long,
)
