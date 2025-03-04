package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchLatestMatchThreadsForTodayUseCase @Inject constructor(
  private val networkDataSource: RedditNetworkDataSource,
) {

  suspend fun execute(): Either<DomainError, List<MatchThreadEntity>> = either {
    networkDataSource.searchFor(
      subreddit = "soccer",
      query = """flair:match+thread AND NOT flair:post AND NOT flair:pre""",
      sortBy = "new",
      timePeriod = "day",
      restrictedToSubreddit = true,
    ).flatMap { it.toMatchThreadEntityList() }.bind()
  }
}
