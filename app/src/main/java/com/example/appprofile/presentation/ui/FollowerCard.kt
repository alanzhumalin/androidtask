package com.example.appprofile.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appprofile.model.Follower
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowerCard(
    follower: Follower?,
    onUnfollow: () -> Unit,
    index: Int = 0,
    isPlaceholder: Boolean = false
) {
    val hasAnimated = remember { mutableStateOf(follower?.appeared == true) }
    val offsetX = remember { Animatable(if (hasAnimated.value) 0f else 60f) }

    LaunchedEffect(follower?.id) {
        if (follower != null && !hasAnimated.value) {
            delay((index * 80).toLong())

            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 450,
                    easing = FastOutSlowInEasing
                )
            )

            follower.appeared = true
            hasAnimated.value = true
        } else {
            offsetX.snapTo(0f)
        }
    }


    if (isPlaceholder) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .offset(x = offsetX.value.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.6f))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.6f)
                        .background(Color.LightGray.copy(alpha = 0.6f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(0.4f)
                        .background(Color.LightGray.copy(alpha = 0.6f))
                    )
                }
            }
        }
        return
    }

    var isFollowing by remember { mutableStateOf(follower?.isFollowing ?: true) }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) Color.Gray else Color(0xFF1E3A8A),
        label = "FollowerButtonColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .offset(x = offsetX.value.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = follower?.avatarRes ?: com.example.appprofile.R.drawable.avatar),
                    contentDescription = follower?.name ?: "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(follower?.name ?: "User", fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = {
                    isFollowing = !isFollowing
                    if (!isFollowing) {
                        onUnfollow()
                    }
                },
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                )
            ) {
                Text(if (isFollowing) "Following" else "Follow")
            }
        }
    }
}
