package com.example.tracker.di

import android.content.Context
import androidx.room.Room
import com.example.tracker.db.RunningDatabase
import com.example.tracker.others.Constants.TRACKER_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTrackerDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,RunningDatabase::class.java,TRACKER_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDAO(db: RunningDatabase) = db.getRunDao()
}