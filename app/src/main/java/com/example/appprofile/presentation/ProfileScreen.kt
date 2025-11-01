package com.example.appprofile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import com.example.appprofile.navigation.Screen
import com.example.appprofile.presentation.ui.FollowerCard
import com.example.appprofile.presentation.ui.ProfileCard
import com.example.appprofile.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.name.value) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.EditProfile.route) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileCard(
                        name = viewModel.name.value,
                        bio = viewModel.bio.value,
                        onFollow = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Followed ${viewModel.name.value}")
                            }
                        }
                    )
                }
            }


            items(viewModel.followers, key = { it.id }) { follower ->
                FollowerCard (
                    follower = follower,
                    onUnfollow = {
                        viewModel.removeFollower(follower)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                "${follower.name} removed",
                                actionLabel = "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed)
                                viewModel.addFollower(follower)
                        }
                    }
                )
            }
        }
    }
}
