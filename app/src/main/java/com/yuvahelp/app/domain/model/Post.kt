package com.yuvahelp.app.domain.model

data class Post(
    val id: Long,
    val title: String,
    val excerpt: String,
    val contentHtml: String,
    val link: String,
    val date: String,
    val featuredImage: String?,
    val category: String
)
