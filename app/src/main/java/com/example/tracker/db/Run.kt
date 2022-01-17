package com.example.tracker.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var image: Bitmap? = null,
    var timestamp: Long = 0L,
    // speed is in km/h
    var avgSpeed: Float = 0F,
    // dsiatnce is in meter
    var distance: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}