package com.example.appprofile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProfileEntity::class,
        FollowerEntity::class,
        PostEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun postDao(): PostDao
}
