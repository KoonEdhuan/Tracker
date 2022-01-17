package com.example.tracker.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.tracker.R
import com.example.tracker.others.Constants
import com.example.tracker.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

    @ServiceScoped
    @Provides
    fun providesFusedLocationProviderClient(
        @ApplicationContext context: Context
    ) =  FusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    fun providesMainActivityPendingIntent(
        @ApplicationContext context: Context
    ) = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )

    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_run)
        .setContentTitle("Tracker app")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)
}