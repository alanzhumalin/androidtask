package com.example.appprofile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
    object Feeds : BottomNavItem("feeds", "Feeds", Icons.Default.List)
}

val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Profile, BottomNavItem.Feeds)
