package dev.iurysouza.livematch.domain.adapters

import arrow.core.Either
import dev.iurysouza.livematch.domain.DomainError

interface NetworkDataSource {
    suspend fun getLatestMatchThreadsForToday(): Either<DomainError, List<MatchThreadEntity>>
    suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity>
    suspend fun getCommentsForSubmission(id: String): Either<DomainError, List<CommentsEntity>>
}
