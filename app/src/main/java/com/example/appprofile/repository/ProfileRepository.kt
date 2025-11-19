package com.example.appprofile.data

import com.example.appprofile.R
import com.example.appprofile.data.local.AppDatabase
import com.example.appprofile.data.local.FollowerEntity
import com.example.appprofile.data.local.ProfileDao
import com.example.appprofile.data.local.ProfileEntity
import com.example.appprofile.data.remote.ApiService
import com.example.appprofile.model.Follower
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ProfileDao
) {

    suspend fun getProfileData(): Pair<String, String> = withContext(Dispatchers.IO) {
        val profile = dao.getProfile()
        profile?.let { it.name to it.bio }
            ?: ("Alan Zhumalin" to "Computer Science Student")
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
        dao.insertFollowers(
            followers.map {
                FollowerEntity(
                    id = it.id,
                    name = it.name,
                    avatarRes = it.avatarRes,
                    isFollowing = it.isFollowing
                )
            }
        )
    }

    suspend fun refreshFollowersFromApi(): List<Follower> = withContext(Dispatchers.IO) {
        val users = api.getUsers()
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
