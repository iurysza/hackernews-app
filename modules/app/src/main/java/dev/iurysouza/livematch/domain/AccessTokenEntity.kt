package dev.iurysouza.livematch.domain

import com.squareup.moshi.Json

data class AccessTokenEntity(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_in") val expiresIn: Long,
    @Json(name = "device_id") val deviceId: String,
    @Json(name = "token_type") val tokenType: String,
    val scope: String,
)
