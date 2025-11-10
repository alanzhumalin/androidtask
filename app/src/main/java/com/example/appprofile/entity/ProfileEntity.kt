package com.example.appprofile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    val name: String,
    val bio: String
)
