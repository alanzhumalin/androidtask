package com.example.appprofile.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appprofile.R
@Composable
fun ProfileCard(
    name: String,
    bio: String,
    onFollow: () -> Unit,
    isSyncing: Boolean,
    isFollowersLoading: Boolean
) {
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableIntStateOf(15) }
    var showUnfollowDialog by remember { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) Color.Gray else Color(0xFF1E3A8A),
        label = "FollowButtonColor"
    )

    val avatarScale by animateFloatAsState(
        targetValue = if (isSyncing) 1.15f else 1f,
        animationSpec = if (isSyncing) {
            tween(durationMillis = 600, easing = FastOutSlowInEasing)
        } else {
            tween(durationMillis = 400, easing = FastOutSlowInEasing)
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    if (isFollowersLoading) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.LightGray.copy(alpha = 0.6f),
                                            Color.LightGray.copy(alpha = 0.3f),
                                            Color.LightGray.copy(alpha = 0.6f)
                                        )
                                    )
                                )
                        )
                    } else {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .graphicsLayer {
                                    scaleX = avatarScale
                                    scaleY = avatarScale
                                }
                                .clip(CircleShape)
                        )
                    }

                    OnlineStatusIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 6.dp, y = 6.dp),
                        visible = !isFollowersLoading
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = name.ifBlank { "—" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = bio.ifBlank { "—" },
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )

                    AnimatedVisibility(
                        visible = !isFollowersLoading,
                        enter = fadeIn(animationSpec = tween(600))
                    ) {
                        Text(
                            text = "Followers: $followerCount",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (isFollowersLoading) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Button(
                onClick = {
                    if (!isFollowing) {
                        isFollowing = true
                        followerCount++
                        onFollow()
                    } else {
                        showUnfollowDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isFollowing) "Unfollow" else "Follow",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    if (showUnfollowDialog) {
        AlertDialog(
            onDismissRequest = { showUnfollowDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    isFollowing = false
                    followerCount = (followerCount - 1).coerceAtLeast(0)
                    showUnfollowDialog = false
                }) { Text("Unfollow") }
            },
            dismissButton = {
                TextButton(onClick = { showUnfollowDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Unfollow User") },
            text = { Text("Are you sure you want to unfollow $name?") }
        )
    }
}


@Composable
fun OnlineStatusIndicator(
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    if (!visible) return

    val pulse by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .graphicsLayer(
                    scaleX = pulse,
                    scaleY = pulse
                )
                .clip(CircleShape)
                .background(Color(0xFF2ECC71))
        )
    }
}
