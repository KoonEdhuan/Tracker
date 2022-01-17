package com.example.tracker.repositores

import com.example.tracker.db.Run
import com.example.tracker.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val runDAO: RunDAO
) {
    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    fun getAllRunsSortedByDate() = runDAO.getAllRunsSortedByDate()

    fun getAllRunsSortedByTime() = runDAO.getAllRunsSortedByTime()

    fun getAllRunsSortedByDistance() = runDAO.getAllRunsSortedByDistance()

    fun getAllRunsSortedByAvgSpeed() = runDAO.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()

    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

    fun getTotalDistance() = runDAO.getTotalDistance()

    fun getTotalTime() = runDAO.getTotalTime()

}