package com.yuvahelp.app.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yuvahelp.app.data.PostRepository
import com.yuvahelp.app.domain.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository.getInstance(application)

    private val searchQuery = MutableStateFlow("")
    val allPosts = repository.observeAllPosts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    val searchResults = searchQuery.flatMapLatest { query ->
        repository.searchPosts(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { repository.syncPosts() }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun selectPost(postId: Long) {
        viewModelScope.launch {
            _selectedPost.value = repository.getPost(postId)
        }
    }
}
