package com.example.appprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import android.content.Context
import androidx.compose.runtime.Composable
import coil.decode.SvgDecoder
import coil.memory.MemoryCache.Builder
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
                val imageLoader = remember { createImageLoader(applicationContext) }

                CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                    MainApp()
                }
            }
        }
    }
}

fun createImageLoader(context: Context): ImageLoader {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .cache(Cache(context.cacheDir, 20L * 1024 * 1024))
        .build()

    return ImageLoader.Builder(context)
        .okHttpClient(okHttpClient)
        .crossfade(true)
        .memoryCache { Builder(context).maxSizePercent(0.25).build() }
        .memoryCache {
            Builder(context)
                .maxSizePercent(0.10)
                .build()
        }
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
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
