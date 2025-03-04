package dev.iurysouza.livematch.common

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import com.squareup.moshi.Moshi
import javax.inject.Inject

interface JsonParser {
  fun <T : Any> toJson(any: T): Either<DomainError, String>
  fun <T> fromJson(json: String, classOfT: Class<T>): Either<MappingError, T>
}

class MoshiJsonParser @Inject constructor(private val moshi: Moshi) : JsonParser {
  override fun <T : Any> toJson(any: T): Either<DomainError, String> = catch {
    moshi.adapter(Any::class.java).toJson(any)
  }.mapLeft { MappingError(it.message) }

  override fun <T> fromJson(json: String, classOfT: Class<T>): Either<MappingError, T> =
    catch {
      moshi.adapter(classOfT).fromJson(json)!!
    }.mapLeft { MappingError(it.message) }
}

inline fun <reified T> JsonParser.fromJson(json: String): Either<MappingError, T> =
  fromJson(json, T::class.java)

inline fun <F : Any, reified T> JsonParser.remap(from: F): Either<DomainError, T> {
  return toJson(from).flatMap {
    fromJson(it, T::class.java)
  }
}
