package com.yuvahelp.app.data.remote

import com.google.gson.annotations.SerializedName

data class WpPostDto(
    val id: Long,
    val date: String,
    val slug: String,
    val link: String,
    val title: Rendered,
    val excerpt: Rendered,
    val content: Rendered,
    @SerializedName("_embedded") val embedded: Embedded?
)

data class Rendered(
    val rendered: String
)

data class Embedded(
    @SerializedName("wp:featuredmedia")
    val featuredMedia: List<FeaturedMedia>?
)

data class FeaturedMedia(
    @SerializedName("source_url")
    val sourceUrl: String?
)
