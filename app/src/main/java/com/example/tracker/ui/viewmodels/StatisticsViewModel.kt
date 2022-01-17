package com.example.tracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tracker.repositores.MainRepository
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
}