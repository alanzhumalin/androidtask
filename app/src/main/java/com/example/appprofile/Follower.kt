package com.example.appprofile

data class Follower(
    val id: Int,
    val name: String,
    val avatarRes: Int,
    var isFollowing: Boolean = false
)