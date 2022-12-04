package dev.iurysouza.livematch.di

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.core.DefaultDispatcherProvider
import dev.iurysouza.livematch.core.DispatcherProvider
import dev.iurysouza.livematch.core.JsonParser
import dev.iurysouza.livematch.core.MoshiJsonParser
import dev.iurysouza.livematch.core.ResourceProvider
import dev.iurysouza.livematch.core.SystemResourceProvider
import dev.iurysouza.livematch.core.storage.KeyValueStorage
import dev.iurysouza.livematch.core.storage.SystemStorage
import dev.iurysouza.livematch.features.matchthread.MatchEventParser
import dev.iurysouza.livematch.features.matchthread.MatchHighlightParser
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    internal fun provideResourceProvider(
        @ApplicationContext appContext: Context,
    ): ResourceProvider = SystemResourceProvider(appContext)

    @Singleton
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("livematch", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun provideSystemStorage(
        sharedPreferences: SharedPreferences,
    ): KeyValueStorage = SystemStorage(sharedPreferences)

    @Provides
    fun provideCoroutineContextProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    internal fun provideMatchHighlightParser(): MatchHighlightParser = MatchHighlightParser()

    @Provides
    @Singleton
    internal fun provideMatchThreadMapper(): MatchEventParser = MatchEventParser()

    @Provides
    @Singleton
    internal fun providesJsonParser(moshi: Moshi): JsonParser = MoshiJsonParser(moshi)
}
