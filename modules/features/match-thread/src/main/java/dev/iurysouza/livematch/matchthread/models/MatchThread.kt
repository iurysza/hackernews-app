package dev.iurysouza.livematch.matchthread.models

import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.domain.models.MediaEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@JsonClass(generateAdapter = true)
data class MatchThread(
  val id: String,
  val title: String,
  val startTime: Long,
  val content: String,
  val homeTeam: Team,
  val awayTeam: Team,
  val refereeList: List<String>,
  val competition: Competition,
)

@JsonClass(generateAdapter = true)
data class MediaItem(
  val title: String,
  val url: String,
  val id: String,
)

@JsonClass(generateAdapter = true)
data class Competition(
  val emblemUrl: String,
  val id: Int?,
  val name: String,
)

@JsonClass(generateAdapter = true)
data class Team(
  val crestUrl: String?,
  val name: String,
  val isHomeTeam: Boolean,
  val isAhead: Boolean,
  val score: String,
)

@JsonClass(generateAdapter = true)
data class MatchEvent(
  val relativeTime: String,
  val icon: EventIcon,
  val description: String,
  val keyEvent: Boolean = false,
)

@JsonClass(generateAdapter = true)
data class CommentItem(
  val author: String,
  val relativeTime: Int?,
  val body: String,
  val score: String,
  val flairUrl: String?,
  val flairName: String,
  val replies: List<CommentItem> = emptyList(),
  val nestedLevel: Int = 0,
)

sealed class ViewError(val message: String) {
  data class CommentItemParsingError(val msg: String) : ViewError(msg)
  data class CommentSectionParsingError(val msg: String) : ViewError(msg)
}

data class CommentSection(
  val name: String,
  val event: MatchEvent,
  val commentList: ImmutableList<CommentItem>,
)

fun List<MediaEntity>.toUi() = map {
  MediaItem(
    title = it.title,
    url = it.url,
    id = it.id,
  )
}.toImmutableList()
