package com.zionhuang.music.di

import android.content.Context
import com.zionhuang.music.db.DatabaseDao
import com.zionhuang.music.db.InternalDatabase
import com.zionhuang.music.db.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMusicDatabase(
        @ApplicationContext context: Context
    ): MusicDatabase {
        return InternalDatabase.newInstance(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseDao(
        database: MusicDatabase
    ): DatabaseDao {
        return database
    }

    @Provides
    @Singleton
    fun provideDatabaseProvider(
        @ApplicationContext context: Context
    ): androidx.media3.database.DatabaseProvider {
        return androidx.media3.database.StandaloneDatabaseProvider(context)
    }

    @Provides
    @Singleton
    fun provideCache(
        @ApplicationContext context: Context,
        databaseProvider: androidx.media3.database.DatabaseProvider
    ): androidx.media3.datasource.cache.Cache {
        val cacheDir = File(context.cacheDir, "downloads")
        return androidx.media3.datasource.cache.SimpleCache(
            cacheDir,
            androidx.media3.datasource.cache.NoOpCacheEvictor(),
            databaseProvider
        )
    }

    @Provides
    @Singleton
    fun provideDownloadManager(
        @ApplicationContext context: Context,
        databaseProvider: androidx.media3.database.DatabaseProvider,
        cache: androidx.media3.datasource.cache.Cache
    ): androidx.media3.exoplayer.offline.DownloadManager {
        return androidx.media3.exoplayer.offline.DownloadManager(
            context,
            databaseProvider,
            cache,
            androidx.media3.datasource.okhttp.OkHttpDataSource.Factory(okhttp3.OkHttpClient()),
            java.util.concurrent.Executor { it.run() }
        )
    }
}

