package com.example.appprofile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followers_table")
data class FollowerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val avatarRes: Int,
    val isFollowing: Boolean
)
