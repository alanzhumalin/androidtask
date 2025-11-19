package com.example.appprofile.navigation

sealed class Screen(val route: String) {
    object Profile : Screen("profile")
    object EditProfile : Screen("editProfile")
}
