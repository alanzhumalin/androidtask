package com.example.appprofile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import com.example.appprofile.viewmodel.PostsViewModel
import androidx.compose.ui.unit.LayoutDirection
import com.example.appprofile.presentation.ui.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedsScreen(viewModel: PostsViewModel, outerPadding: PaddingValues) {

    val posts = viewModel.posts

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Feeds") })
        }
    ) { innerPadding ->
        val combined = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + outerPadding.calculateBottomPadding(),
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + outerPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + outerPadding.calculateEndPadding(LayoutDirection.Ltr)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(combined),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(posts, key = { it.id }) { post ->
                PostItem(post)
            }
        }
    }
}
