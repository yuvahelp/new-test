package com.yuvahelp.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuvahelp.app.domain.model.Post
import com.yuvahelp.app.ui.components.PostCard

@Composable
fun CategoriesScreen(posts: List<Post>, onPostClick: (Long) -> Unit) {
    val categories = listOf("Latest Jobs", "Results", "Admit Cards", "Government Schemes", "Education News")
    var selected by remember { mutableStateOf(categories.first()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Browse by Category", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        AssistChip(
                            onClick = { selected = category },
                            label = { Text(category) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selected == category) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }
            items(posts.filter { it.category == selected }) { post ->
                PostCard(post = post, onClick = { onPostClick(post.id) })
            }
        }
    }
}
