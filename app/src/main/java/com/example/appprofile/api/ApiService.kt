package com.example.appprofile.data.remote

import com.example.appprofile.model.Post
import retrofit2.http.GET

data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}
