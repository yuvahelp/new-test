package com.yuvahelp.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val excerpt: String,
    val contentHtml: String,
    val link: String,
    val slug: String,
    val date: String,
    val featuredImage: String?,
    val category: String
)
