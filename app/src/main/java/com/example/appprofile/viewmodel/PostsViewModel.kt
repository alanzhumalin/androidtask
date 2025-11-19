package com.example.appprofile.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprofile.data.PostsRepository
import com.example.appprofile.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostsRepository
) : ViewModel() {

    val posts = mutableStateListOf<Post>()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            val saved = repository.getSavedPosts()
            if (saved.isNotEmpty()) {
                posts.clear()
                posts.addAll(saved)
            } else {
                refresh()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val fresh = repository.fetchPosts()
            posts.clear()
            posts.addAll(fresh)
        }
    }
}
