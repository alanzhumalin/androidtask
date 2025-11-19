package com.example.appprofile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.PaddingValues
import com.example.appprofile.viewmodel.ProfileViewModel
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    outerPadding: PaddingValues
) {
    var name by remember { mutableStateOf(viewModel.name.value) }
    var bio by remember { mutableStateOf(viewModel.bio.value) }

    LaunchedEffect(viewModel.name.value, viewModel.bio.value) {
        name = viewModel.name.value
        bio = viewModel.bio.value
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Edit Profile") }) }
    ) { innerPadding ->
        val combined = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + outerPadding.calculateBottomPadding(),
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + outerPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + outerPadding.calculateEndPadding(LayoutDirection.Ltr)
        )

        Column(
            modifier = Modifier
                .padding(combined)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.updateProfile(name, bio)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}
