package com.example.tracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(Converter::class)
public abstract class RunningDatabase : RoomDatabase() {

    abstract fun getRunDao(): RunDAO
}