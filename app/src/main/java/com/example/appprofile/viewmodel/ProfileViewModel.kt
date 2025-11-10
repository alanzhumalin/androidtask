package com.example.appprofile.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprofile.model.Follower
import com.example.appprofile.data.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = ProfileRepository(app)

    var name = mutableStateOf("")
    var bio = mutableStateOf("")
    var followers = mutableStateListOf<Follower>()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val (savedName, savedBio) = repository.getProfileData()
            name.value = savedName
            bio.value = savedBio
            followers.clear()
            followers.addAll(repository.getFollowers())
            if (followers.isEmpty()) {
                followers.addAll(repository.refreshFollowersFromApi())
                repository.saveFollowers(followers)
            }
        }
    }

    fun updateProfile(newName: String, newBio: String) {
        name.value = newName
        bio.value = newBio
        viewModelScope.launch {
            repository.saveProfile(newName, newBio)
        }
    }

    fun removeFollower(follower: Follower) {
        followers.remove(follower)
        viewModelScope.launch {
            repository.saveFollowers(followers)
        }
    }

    fun addFollower(follower: Follower) {
        followers.add(follower)
        viewModelScope.launch {
            repository.saveFollowers(followers)
        }
    }

    fun refreshFollowers() {
        viewModelScope.launch {
            val newFollowers = repository.refreshFollowersFromApi()
            followers.clear()
            followers.addAll(newFollowers)
            repository.saveFollowers(newFollowers)
        }
    }
}
