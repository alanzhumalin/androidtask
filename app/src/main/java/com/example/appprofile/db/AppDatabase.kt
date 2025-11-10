package com.example.appprofile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProfileEntity::class, FollowerEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}
