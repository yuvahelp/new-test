package com.yuvahelp.app.data

import android.content.Context
import androidx.room.Room
import com.yuvahelp.app.data.local.AppDatabase
import com.yuvahelp.app.data.local.PostEntity
import com.yuvahelp.app.data.remote.WordPressApi
import com.yuvahelp.app.domain.model.Post
import com.yuvahelp.app.util.HtmlUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostRepository private constructor(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "yuva-help-db"
    ).fallbackToDestructiveMigration().build()

    private val api: WordPressApi by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder().addInterceptor(logger).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordPressApi::class.java)
    }

    fun observeAllPosts(): Flow<List<Post>> = db.postDao().observeAll().map { it.map(PostEntity::toDomain) }

    fun observePostsByCategory(category: String): Flow<List<Post>> =
        db.postDao().observeByCategory(category).map { it.map(PostEntity::toDomain) }

    fun searchPosts(query: String): Flow<List<Post>> =
        db.postDao().search(query).map { it.map(PostEntity::toDomain) }

    suspend fun getPost(id: Long): Post? = db.postDao().getById(id)?.toDomain()

    suspend fun syncPosts(): Int {
        val remotePosts = api.getPosts()
        val entities = remotePosts.map { dto ->
            val title = HtmlUtils.htmlToPlainText(dto.title.rendered)
            val excerpt = HtmlUtils.htmlToPlainText(dto.excerpt.rendered)
            PostEntity(
                id = dto.id,
                title = title,
                excerpt = excerpt,
                contentHtml = dto.content.rendered,
                link = dto.link,
                slug = dto.slug,
                date = dto.date,
                featuredImage = dto.embedded?.featuredMedia?.firstOrNull()?.sourceUrl,
                category = HtmlUtils.inferCategory(title, excerpt)
            )
        }
        db.postDao().insertAll(entities)
        return entities.size
    }

    suspend fun hasNewPosts(newestFetchedId: Long): Boolean {
        val latest = db.postDao().getLatestPostId() ?: return true
        return newestFetchedId > latest
    }

    companion object {
        private const val BASE_URL = "https://yuva.help/"

        @Volatile
        private var INSTANCE: PostRepository? = null

        fun getInstance(context: Context): PostRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PostRepository(context).also { INSTANCE = it }
            }
        }
    }
}

private fun PostEntity.toDomain(): Post = Post(
    id = id,
    title = title,
    excerpt = excerpt,
    contentHtml = contentHtml,
    link = link,
    date = date,
    featuredImage = featuredImage,
    category = category
)
