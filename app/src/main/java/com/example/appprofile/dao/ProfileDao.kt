package com.example.appprofile.data.local

import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile_table LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM followers_table")
    suspend fun getFollowers(): List<FollowerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowers(followers: List<FollowerEntity>)

    @Query("DELETE FROM followers_table")
    suspend fun clearFollowers()
}
