package dev.iurysouza.livematch.matchday.models

import kotlin.random.Random
import kotlin.random.nextInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Suppress("MagicNumber")
object Fakes {
  private const val DEFAULT = 10
  fun generateMatchList(count: Int = DEFAULT): ImmutableList<MatchUiModel> {
    val list = mutableListOf<MatchUiModel>()
    repeat(count / 2) { list.add(generateMatch(it)) }
    repeat(count / 2) { list.add(generateMatch(it)) }
    return list.toImmutableList()
  }

  private fun generateMatch(seed: Int): MatchUiModel {
    val homeScore = (0..5).random()
    val awayScore = (0..5).random()
    return MatchUiModel(
      id = seed.toString(),
      homeTeam = Team(
        crestUrl = "https://crests.football-data.org/770.svg",
        randomWordWithMaxChars(15),
        isHomeTeam = true,
        isAhead = homeScore > awayScore,
        score = "$homeScore",
      ),
      awayTeam = Team(
        crestUrl = "https://crests.football-data.org/770.svg",
        randomWordWithMaxChars(15),
        isHomeTeam = false,
        isAhead = homeScore < awayScore,
        score = "$awayScore",
      ),
      startTime = "${(14..20).random()}:00",
      elapsedMinutes = "${(0..90).random()}",
      competition = competition(randomCompetition()),
    )
  }

  private fun randomCompetition(): String {
    return if (Random.nextInt() % 2 == 0) {
      "Premier League"
    } else {
      "Champions League"
    }
  }

  fun generateMatchThread(): MatchThread {
    return MatchThread(
      id = "vero",
      title = "ius",
      startTime = 8586,
      content = "adolescens",
      homeTeam = Team(
        crestUrl = null,
        name = "Rodrick Bray",
        isHomeTeam = false,
        isAhead = false,
        score = "1",
      ),
      awayTeam = Team(
        crestUrl = null,
        name = "Rosetta Sexton",
        isHomeTeam = false,
        isAhead = false,
        score = "3",
      ),
      refereeList = listOf(),
      competition = Competition(
        emblemUrl = "https://duckduckgo.com/?q=torquent",
        id = null,
        name = "Ariel Barr",
      ),

    )
  }

  private fun randomWordWithMaxChars(max: Int): String =
    buildString {
      repeat(Random.nextInt(5, max)) {
        append(('a'..'z').random())
      }
    }
}

private fun competition(name: String, id: Int = 12) = Competition(
  name = name,
  id = id,
  emblemUrl = "https://crests.football-data.org/770.svg",
)

fun IntRange.random() = Random.nextInt((endInclusive + 1) - start) + start
