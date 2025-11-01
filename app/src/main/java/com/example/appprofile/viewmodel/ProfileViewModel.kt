package com.example.appprofile.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.appprofile.R
import com.example.appprofile.model.Follower

class ProfileViewModel : ViewModel() {
    var name = mutableStateOf("Alan Zhumalin")
    var bio = mutableStateOf("Computer Science Student")
    var followers = mutableStateListOf<Follower>()

    init {
        repeat(10) { index ->
            followers.add(
                Follower(
                    id = index,
                    name = "Follower #${index + 1}",
                    avatarRes = R.drawable.avatar,
                    isFollowing = true
                )
            )
        }
    }

    fun updateProfile(newName: String, newBio: String) {
        name.value = newName
        bio.value = newBio
    }

    fun removeFollower(follower: Follower) {
        followers.remove(follower)
    }

    fun addFollower(follower: Follower) {
        followers.add(follower)
    }
}