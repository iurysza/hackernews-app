package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.layout.Column
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
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.features.matchlist.Team
import dev.iurysouza.livematch.ui.features.matchthread.components.CommentSectionComponent
import dev.iurysouza.livematch.ui.features.matchthread.components.MatchDescription
import dev.iurysouza.livematch.ui.features.matchthread.components.MatchHeader
import dev.iurysouza.livematch.ui.theme.ColorPrimary

@Composable
fun MatchThreadScreen(
    matchThread: MatchThread,
    navigateUp: () -> Unit,
) {
    val viewModel = hiltViewModel<MatchThreadViewModel>()
    LaunchedEffect(Unit) {
        viewModel.update(matchThread)
    }

    val commentsState = viewModel.commentsState.collectAsState().value
    val state = viewModel.state.collectAsState().value

    MatchThread(navigateUp, state, commentsState)

}

@Composable
private fun MatchThread(
    navigateUp: () -> Unit = {},
    state: MatchDescriptionState,
    commentsState: MatchCommentsState,
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
                title = { Text(text = stringResource(R.string.match_thread)) },
                backgroundColor = ColorPrimary,
            )
        },
    ) {
        Column {
            when (state) {
                MatchDescriptionState.Loading -> FullScreenProgress()
                is MatchDescriptionState.Error -> ErrorScreen(state.msg)
                is MatchDescriptionState.Success -> {
                    MatchHeader(
                        homeTeam = state.matchThread.awayTeam,
                        awayTeam = state.matchThread.homeTeam,
                    )
                    MatchDescription(
                        description = state.matchThread.content ?: "",
                        mediaList = state.matchThread.mediaList,
                    )
                }
            }
            when (commentsState) {
                MatchCommentsState.Loading -> FullScreenProgress()
                is MatchCommentsState.Error -> ErrorScreen(commentsState.msg)
                is MatchCommentsState.Success -> {
                    CommentSectionComponent(
                        commentSectionList = commentsState.commentSectionList,
                        onClick = {}
                    )
                }
            }
        }
    }

}


@Preview
@Composable
private fun MatchThreadPreview() {
    MatchThreadScreen(
        matchThread = MatchThread(
            competition = Competition(
                emblemUrl = "",
                id = null,
                name = ""
            ),
            content = "Real Madrid",
            id = "id",
            startTime = 9,
            mediaList = emptyList(),
            homeTeam = Team(
                crestUrl = null,
                name = "",
                isHomeTeam = false,
                isAhead = false,
                score = ""
            ),
            awayTeam = Team(
                crestUrl = null,
                name = "",
                isHomeTeam = false,
                isAhead = false,
                score = ""
            ),
            refereeList = listOf(),
        ),
        navigateUp = {}
    )
}
