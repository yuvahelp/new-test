package com.yuvahelp.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WordPressApi {
    @GET("wp-json/wp/v2/posts")
    suspend fun getPosts(
        @Query("_embed") embed: String = "1",
        @Query("per_page") perPage: Int = 25,
        @Query("search") search: String? = null,
        @Query("categories") categoryId: Int? = null
    ): List<WpPostDto>
}
