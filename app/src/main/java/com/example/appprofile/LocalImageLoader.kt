package com.example.appprofile

import androidx.compose.runtime.staticCompositionLocalOf
import coil.ImageLoader

val LocalImageLoader = staticCompositionLocalOf<ImageLoader> {
    error("No ImageLoader provided")
}
