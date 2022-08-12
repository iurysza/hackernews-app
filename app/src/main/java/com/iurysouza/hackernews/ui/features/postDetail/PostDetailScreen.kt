package com.iurysouza.hackernews.ui.features.postDetail

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.iurysouza.hackernews.R
import com.iurysouza.hackernews.ui.components.ErrorScreen
import com.iurysouza.hackernews.ui.components.FullScreenProgress
import com.iurysouza.hackernews.ui.features.posts.Post
import com.iurysouza.hackernews.ui.theme.ColorPrimary

@Composable
fun PostDetailScreen(
    navigateUp: () -> Unit,
    post: Post,
) {
    val viewModel = hiltViewModel<PostsDetailViewModel>()
    LaunchedEffect(Unit) {
        viewModel.loadPostDetail(post)
    }

    when (val state = viewModel.state.collectAsState().value) {
        is PostDetailScreenState.Success -> PostDetail(state.author, state.post, navigateUp)
        PostDetailScreenState.Loading -> FullScreenProgress()
        is PostDetailScreenState.Error -> ErrorScreen(state.msg)
    }
}


@Composable
private fun PostDetail(
    user: User,
    post: Post,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_description),
                        )
                    }
                },
                title = { Text(text = stringResource(R.string.posts_detail)) },
                backgroundColor = ColorPrimary,
            )
        },
    ) {
        PostDetailContent(post, user)
    }
}


@Preview
@Composable
private fun PostDetailPreview() {
    PostDetail(
        user = User(
            id = 0,
            name = "name",
            username = "username",
            email = "name@email.com",
            website = "name.com"
        ),
        post = Post(
            body = "do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            id = 0,
            userId = 0,
            title = "Lorem ipsum dolor sit amet",
            bgColor = 0xFF33A369
        )
    ) {}
}
