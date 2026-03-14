package com.yuvahelp.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuvahelp.app.domain.model.Post
import com.yuvahelp.app.ui.components.PostCard

@Composable
fun HomeScreen(posts: List<Post>, onPostClick: (Long) -> Unit) {
    val sections = listOf("Latest Jobs", "Results", "Admit Cards", "Government Schemes", "Education News")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sections.forEach { section ->
            item {
                Text(section, style = MaterialTheme.typography.titleLarge)
            }
            items(posts.filter { it.category == section }.take(4)) { post ->
                PostCard(post = post, onClick = { onPostClick(post.id) })
            }
        }
    }
}
