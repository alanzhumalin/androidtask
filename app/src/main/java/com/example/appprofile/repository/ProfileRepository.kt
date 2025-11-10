package com.example.appprofile.data

import android.content.Context
import androidx.room.Room
import com.example.appprofile.R
import com.example.appprofile.data.local.*
import com.example.appprofile.data.remote.RetrofitInstance
import com.example.appprofile.model.Follower
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "profile_db"
    ).build()

    private val dao = db.profileDao()

    suspend fun getProfileData(): Pair<String, String> = withContext(Dispatchers.IO) {
        val profile = dao.getProfile()
        profile?.let { it.name to it.bio } ?: ("Alan Zhumalin" to "Computer Science Student")
    }

    suspend fun getFollowers(): List<Follower> = withContext(Dispatchers.IO) {
        dao.getFollowers().map {
            Follower(it.id, it.name, it.avatarRes, it.isFollowing)
        }
    }

    suspend fun saveProfile(name: String, bio: String) = withContext(Dispatchers.IO) {
        dao.insertProfile(ProfileEntity(id = 1, name = name, bio = bio))
    }

    suspend fun saveFollowers(followers: List<Follower>) = withContext(Dispatchers.IO) {
        dao.clearFollowers()
        dao.insertFollowers(followers.map {
            FollowerEntity(it.id, it.name, it.avatarRes, it.isFollowing)
        })
    }

    suspend fun refreshFollowersFromApi(): List<Follower> = withContext(Dispatchers.IO) {
        val users = RetrofitInstance.api.getUsers()
        users.take(10).map {
            Follower(
                id = it.id,
                name = it.name,
                avatarRes = R.drawable.avatar,
                isFollowing = true
            )
        }
    }
}
