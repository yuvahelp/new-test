package com.yuvahelp.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("SELECT * FROM posts ORDER BY date DESC")
    fun observeAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE category = :category ORDER BY date DESC")
    fun observeByCategory(category: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getById(id: Long): PostEntity?

    @Query("SELECT * FROM posts WHERE title LIKE '%' || :keyword || '%' OR excerpt LIKE '%' || :keyword || '%' ORDER BY date DESC")
    fun search(keyword: String): Flow<List<PostEntity>>

    @Query("SELECT MAX(id) FROM posts")
    suspend fun getLatestPostId(): Long?
}
