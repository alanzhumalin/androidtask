package com.example.appprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.appprofile.navigation.AppNavGraph
import com.example.appprofile.ui.theme.AppProfileTheme
import com.example.appprofile.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppProfileTheme {
                val navController = rememberNavController()
                val viewModel: ProfileViewModel = viewModel()
                AppNavGraph(navController, viewModel)
            }
        }
    }
}
