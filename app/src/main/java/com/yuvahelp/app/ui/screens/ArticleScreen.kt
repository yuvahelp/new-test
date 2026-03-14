package com.yuvahelp.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.yuvahelp.app.domain.model.Post

@Composable
fun ArticleScreen(post: Post?) {
    val context = LocalContext.current

    if (post == null) {
        Text("Loading article...", modifier = Modifier.padding(16.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(model = post.featuredImage, contentDescription = post.title)
        Text(post.title, style = MaterialTheme.typography.headlineSmall)
        Text(post.date, style = MaterialTheme.typography.labelMedium)

        AndroidView(
            factory = { ctx ->
                TextView(ctx).apply {
                    textSize = 16f
                    movementMethod = LinkMovementMethod.getInstance()
                }
            },
            update = { view -> view.text = android.text.Html.fromHtml(post.contentHtml, android.text.Html.FROM_HTML_MODE_COMPACT) }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${post.title}\n${post.link}")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }) {
                Text("Share")
            }
            Button(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.link)))
            }) {
                Text("Open Original")
            }
        }
    }
}
