package com.example.appprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appprofile.ui.theme.AppProfileTheme
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppProfileTheme {
                ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val stories = listOf(
        R.drawable.avatar, R.drawable.avatar, R.drawable.avatar,
        R.drawable.avatar, R.drawable.avatar
    )

    var followers by rememberSaveable {
        mutableStateOf(
            List(10) { index ->
                Follower(
                    id = index,
                    name = "Follower #${index+1}",
                    avatarRes = R.drawable.avatar,
                    isFollowing = true
                )
            }
        )
    }

    var recentlyRemoved by remember { mutableStateOf<Follower?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Screen") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF3F4F6)),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                StoriesCarousel(stories)
            }

            item {
                ProfileCard(
                    onFollow = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "You followed Alan!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }

            items(followers, key = { it.id }) { follower ->
                FollowerCard(
                    follower = follower,
                    onUnfollow = {
                        followers = followers - follower
                        recentlyRemoved = follower

                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "${follower.name} removed",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed && recentlyRemoved != null) {
                                followers = followers + recentlyRemoved!!
                                recentlyRemoved = null
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StoriesCarousel(stories: List<Int>) {
    Text(
        text = "Stories",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(stories.size) { index ->
            Image(
                painter = painterResource(id = stories[index]),
                contentDescription = "Story",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun FollowerCard(follower: Follower, onUnfollow: () -> Unit) {
    var isFollowing by rememberSaveable { mutableStateOf(follower.isFollowing) }
    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) Color.Gray else Color(0xFF1E3A8A),
        label = "FollowerButtonColor"
    )

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onUnfollow()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Removing...",
                    color = Color(0xFFFF4444),
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
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
                            painter = painterResource(id = follower.avatarRes),
                            contentDescription = follower.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(55.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(follower.name, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = { isFollowing = !isFollowing },
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
    )
}

@Composable
fun ProfileCard(onFollow: () -> Unit) {
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableIntStateOf(15) }
    var showUnfollowDialog by remember { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) Color.Gray else Color(0xFF1E3A8A),
        label = "FollowButtonColor"
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
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Alan Zhumalin",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Computer Science Student",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Followers: $followerCount",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
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
                }) {
                    Text("Unfollow")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnfollowDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Unfollow User") },
            text = { Text("Are you sure you want to unfollow Alan Zhumalin?") }
        )
    }
}
