package com.example.appprofile.model

data class Follower(
    val id: Int,
    val name: String,
    val avatarRes: Int,
    var isFollowing: Boolean = false,
    var appeared: Boolean = false

)