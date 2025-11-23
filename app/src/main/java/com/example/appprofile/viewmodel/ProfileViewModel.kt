package com.example.appprofile.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprofile.data.ProfileRepository
import com.example.appprofile.model.Follower
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    var name = mutableStateOf("")
    var bio = mutableStateOf("")
    var followers = mutableStateListOf<Follower>()

    var isSyncing = mutableStateOf(false)
    var isFollowersLoading = mutableStateOf(true)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val (savedName, savedBio) = repository.getProfileData()
            name.value = savedName
            bio.value = savedBio

            isFollowersLoading.value = true
            followers.clear()
            val savedFollowers = repository.getFollowers()
            if (savedFollowers.isNotEmpty()) {
                followers.addAll(savedFollowers)
            } else {
                val fresh = repository.refreshFollowersFromApi()
                followers.addAll(fresh)
                repository.saveFollowers(fresh)
            }
            delay(150)
            isFollowersLoading.value = false
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
            isSyncing.value = true
            isFollowersLoading.value = true

            val fresh = repository.refreshFollowersFromApi()
            followers.clear()
            followers.addAll(fresh)
            repository.saveFollowers(fresh)

            delay(350)
            isFollowersLoading.value = false
            isSyncing.value = false
        }
    }
}
