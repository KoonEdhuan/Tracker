package com.example.tracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tracker.repositores.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class MainViewModel @Inject constructor(
    val  mainRepository: MainRepository
) : ViewModel() {
}