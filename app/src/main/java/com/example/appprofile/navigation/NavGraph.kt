package com.example.appprofile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appprofile.presentation.EditProfileScreen
import com.example.appprofile.presentation.HomeScreen
import com.example.appprofile.presentation.ProfileScreen
import com.example.appprofile.viewmodel.ProfileViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object EditProfile : Screen("editProfile")
}

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: ProfileViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController, viewModel) }
        composable(Screen.EditProfile.route) { EditProfileScreen(navController, viewModel) }
    }
}
