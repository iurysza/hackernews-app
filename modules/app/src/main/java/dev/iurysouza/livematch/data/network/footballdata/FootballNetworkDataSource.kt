package dev.iurysouza.livematch.data.network.footballdata

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import dev.iurysouza.livematch.domain.adapters.NetworkError
import javax.inject.Inject

class FootballNetworkDataSource @Inject constructor(
    private val api: FootballDataApi,
) : FootballDataSource {
    override suspend fun fetchLatestMatches(
        startDate: String,
        endDate: String,
    ): Either<NetworkError, MatchListResponse> = catch {
        api.fetchLatestMatches(
            dateFrom = startDate,
            dateTo = endDate,
        )
    }.mapLeft { NetworkError(it.message) }

}
