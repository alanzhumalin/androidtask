package com.example.appprofile.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appprofile.navigation.bottomNavItems
import com.example.appprofile.navigation.BottomNavItem
import com.example.appprofile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarScaffold(
    navController: NavController,
    content: @Composable (paddingValues: PaddingValues, profileViewModel: ProfileViewModel) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val profileViewModel: ProfileViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray
                        )
                    )
                }
            }
        },
        content = { padding ->
            content(padding, profileViewModel)
        }
    )
}
