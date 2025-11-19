package com.example.appprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.appprofile.navigation.AppNavGraph
import com.example.appprofile.ui.theme.AppProfileTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.appprofile.ui.BottomBarScaffold

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppProfileTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    BottomBarScaffold(navController = navController) { padding, profileViewModel ->
        AppNavGraph(
            navController = navController,
            padding = padding,
            profileViewModel = profileViewModel
        )
    }
}
