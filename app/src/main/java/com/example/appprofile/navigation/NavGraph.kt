package com.example.appprofile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appprofile.presentation.EditProfileScreen
import com.example.appprofile.presentation.FeedsScreen
import com.example.appprofile.presentation.HomeScreen
import com.example.appprofile.presentation.ProfileScreen
import com.example.appprofile.viewmodel.ProfileViewModel
import com.example.appprofile.viewmodel.PostsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    profileViewModel: ProfileViewModel,
    startDestination: String = BottomNavItem.Home.route
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(BottomNavItem.Home.route) {
            HomeScreen(
                navController = navController,
                outerPadding = padding
            )
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel,
                outerPadding = padding
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                navController = navController,
                viewModel = profileViewModel,
                outerPadding = padding
            )
        }

        composable(BottomNavItem.Feeds.route) {
            val postsViewModel: PostsViewModel = hiltViewModel()
            FeedsScreen(
                viewModel = postsViewModel,
                outerPadding = padding
            )
        }
    }
}
