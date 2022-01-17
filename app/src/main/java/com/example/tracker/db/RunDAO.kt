package com.example.tracker.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("select * from running_table order by timestamp desc")
    fun getAllRunsSortedByDate(): LiveData<List<Run>>

    @Query("select * from running_table order by timeInMillis desc")
    fun getAllRunsSortedByTime(): LiveData<List<Run>>

    @Query("select * from running_table order by caloriesBurned desc")
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>

    @Query("select * from running_table order by avgSpeed desc")
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>

    @Query("select * from running_table order by distance desc")
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>


    @Query("select sum(timeInMillis) from running_table")
    fun getTotalTime(): LiveData<Long>

    @Query("select sum(caloriesBurned) from running_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("select sum(distance) from running_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("select avg(avgSpeed) from running_table")
    fun getTotalAvgSpeed(): LiveData<Float>

}