package com.example.appprofile.data

import com.example.appprofile.data.local.PostDao
import com.example.appprofile.data.local.PostEntity
import com.example.appprofile.data.remote.ApiService
import com.example.appprofile.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val api: ApiService,
    private val dao: PostDao
) {

    suspend fun getSavedPosts(): List<Post> = withContext(Dispatchers.IO) {
        dao.getPosts().map { Post(it.id, it.title, it.body) }
    }

    suspend fun fetchPosts(): List<Post> = withContext(Dispatchers.IO) {
        val posts = api.getPosts().take(20)
        dao.clearPosts()
        dao.insertPosts(
            posts.map { PostEntity(it.id, it.title, it.body) }
        )
        posts
    }
}
