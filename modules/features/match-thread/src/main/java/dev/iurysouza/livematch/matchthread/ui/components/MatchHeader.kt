package dev.iurysouza.livematch.matchthread.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.AsyncImage
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchHeader

@Composable
fun MatchHeader(matchHeader: MatchHeader) {
  val awayTeam = matchHeader.awayTeam
  val homeTeam = matchHeader.homeTeam
  Row(
    verticalAlignment = Alignment.Bottom,
    horizontalArrangement = Arrangement.Center,
    modifier = Modifier
      .background(MaterialTheme.colors.background)
      .padding(vertical = 32.dp)
      .fillMaxWidth(),
  ) {
    HomeMatchHeader(
      team = homeTeam.name,
      teamCrestUrl = homeTeam.crestUrl,
      score = homeTeam.score,
    )
    Text(
      text = "x",
      style = TextStyle(color = MaterialTheme.colors.onPrimary),
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .padding(bottom = 16.dp),
    )
    AwayMatchHeader(
      team = awayTeam.name,
      teamCrestUrl = awayTeam.crestUrl,
      score = awayTeam.score,
    )
  }
}

@Composable
fun HomeMatchHeader(
  team: String,
  teamCrestUrl: String,
  score: String,
) {
  val constraintSet = ConstraintSet {
    val nameConstraint = createRefFor("name")
    val scoreConstraint = createRefFor("score")
    val crestConstraint = createRefFor("crest")
    constrain(nameConstraint) {
      top.linkTo(parent.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(crestConstraint.top)
    }
    constrain(crestConstraint) {
      top.linkTo(nameConstraint.bottom)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(parent.bottom)
    }
    constrain(scoreConstraint) {
      top.linkTo(crestConstraint.top)
      bottom.linkTo(crestConstraint.bottom)
      start.linkTo(crestConstraint.end)
      end.linkTo(parent.end)
    }
  }
  HeaderContent(
    constraintSet = constraintSet,
    team = team,
    teamCrestUrl = teamCrestUrl,
    score = score,
    modifier = Modifier.padding(start = 36.dp),
  )
}

@Composable
fun AwayMatchHeader(
  team: String,
  teamCrestUrl: String,
  score: String,
) {
  val constraintSet = ConstraintSet {
    val nameConstraint = createRefFor("name")
    val scoreConstraint = createRefFor("score")
    val crestConstraint = createRefFor("crest")
    constrain(nameConstraint) {
      top.linkTo(parent.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(crestConstraint.top)
    }
    constrain(crestConstraint) {
      top.linkTo(nameConstraint.bottom)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(parent.bottom)
    }
    constrain(scoreConstraint) {
      top.linkTo(crestConstraint.top)
      bottom.linkTo(crestConstraint.bottom)
      start.linkTo(parent.start)
      end.linkTo(crestConstraint.start)
    }
  }
  HeaderContent(
    team = team,
    score = score,
    constraintSet = constraintSet,
    teamCrestUrl = teamCrestUrl,
    modifier = Modifier.padding(end = 36.dp),
  )
}

@Composable
private fun HeaderContent(
  constraintSet: ConstraintSet,
  team: String,
  teamCrestUrl: String,
  score: String,
  modifier: Modifier = Modifier,
) {
  ConstraintLayout(
    constraintSet = constraintSet,
    modifier = Modifier.wrapContentSize(),
  ) {
    val title = TextStyle(color = MaterialTheme.colors.onPrimary)
    Box(
      modifier = Modifier
        .layoutId("name")
        .padding(bottom = 8.dp),
    ) {
      Team(team = team, title)
    }
    Box(
      modifier = Modifier.layoutId("crest"),
    ) {
      TeamCrest(teamCrestUrl = teamCrestUrl)
    }
    Box(
      modifier = modifier
        .layoutId("score"),
    ) {
      Score(score = score, title)
    }
  }
}

@Composable
private fun Score(score: String, title: TextStyle) {
  Text(text = score, style = title.copy(fontSize = 35.sp))
}

@Composable
private fun Team(team: String, style: TextStyle) {
  Text(
    modifier = Modifier.widthIn(max = 150.dp),
    text = team,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colors.onPrimary),
  )
}

@Composable
fun TeamCrest(
  teamCrestUrl: String,
  modifier: Modifier = Modifier,
) {
  Box {
    Box(
      modifier = modifier
        .size(56.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colors.onPrimary),
    )
    AsyncImage(
      modifier = modifier
        .size(56.dp)
        .clip(CircleShape)
        .border(width = 2.dp, MaterialTheme.colors.onPrimary, CircleShape),
      contentScale = ContentScale.Crop,
      model = teamCrestUrl,
      contentDescription = "teamCrest",
    )
  }
}

@Composable
@Preview
private fun MatchHeaderPreview() = LiveMatchThemePreview {
  MatchHeader(FakeFactory.generateMatchHeader())
}
