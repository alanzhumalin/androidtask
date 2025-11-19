package com.example.appprofile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.example.appprofile.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, outerPadding: PaddingValues) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) }
    ) { innerPadding ->

        val combined = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + outerPadding.calculateBottomPadding(),
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) +
                    outerPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) +
                    outerPadding.calculateEndPadding(LayoutDirection.Ltr)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(combined),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate(Screen.Profile.route) }) {
                Text("Go to Profile")
            }
        }
    }
}
