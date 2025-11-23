package com.example.appprofile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.PaddingValues
import com.example.appprofile.navigation.Screen
import com.example.appprofile.presentation.ui.FollowerCard
import com.example.appprofile.presentation.ui.ProfileCard
import com.example.appprofile.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    outerPadding: PaddingValues
) {
    val name by viewModel.name
    val bio by viewModel.bio
    val followers = viewModel.followers

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name) },
                actions = {
                    IconButton(onClick = {
                        viewModel.refreshFollowers()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.EditProfile.route)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val combined = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + outerPadding.calculateBottomPadding(),
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + outerPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + outerPadding.calculateEndPadding(LayoutDirection.Ltr)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(combined),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileCard(
                        name = name,
                        bio = bio,
                        onFollow = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Followed $name")
                            }
                        },
                        isSyncing = viewModel.isSyncing.value,
                        isFollowersLoading = viewModel.isFollowersLoading.value
                    )
                }
            }

            if (viewModel.isFollowersLoading.value) {
                items(4) { index ->
                    FollowerCard(
                        follower = null,
                        onUnfollow = {},
                        index = index,
                        isPlaceholder = true
                    )
                }
            } else {
                itemsIndexed(followers, key = { _, it -> it.id }) { index, follower ->
                    FollowerCard(
                        follower = follower,
                        onUnfollow = {
                            viewModel.removeFollower(follower)
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    "${follower.name} unfollowed",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.addFollower(follower)
                                }
                            }
                        },
                        index = index,
                        isPlaceholder = false
                    )
                }
            }
        }
    }
}
