package dev.iurysouza.livematch.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

interface DispatcherProvider {
  fun main(): CoroutineDispatcher
  fun default(): CoroutineDispatcher
  fun io(): CoroutineDispatcher
  fun unconfined(): CoroutineDispatcher
}

@Singleton
class DefaultDispatcherProvider : DispatcherProvider {
  override fun main(): CoroutineDispatcher = Dispatchers.Main
  override fun default(): CoroutineDispatcher = Dispatchers.Default
  override fun io(): CoroutineDispatcher = Dispatchers.IO
  override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}
