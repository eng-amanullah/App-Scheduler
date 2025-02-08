package com.amanullah.appscheduler.presentation.completed

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.domain.GetAllCompletedAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletedViewModel @Inject constructor(private val getAllCompletedAlarmUseCase: GetAllCompletedAlarmUseCase) :
    ViewModel() {
    val completedAlarm = mutableStateOf<List<Alarm>>(value = emptyList())

    fun fetchCompletedAlarms() {
        viewModelScope.launch {
            completedAlarm.value = getAllCompletedAlarmUseCase()
        }
    }
}