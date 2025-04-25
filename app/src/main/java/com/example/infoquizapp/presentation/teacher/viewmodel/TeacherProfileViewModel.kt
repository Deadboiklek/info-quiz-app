package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import com.example.infoquizapp.data.teacher.model.TeacherUpdate
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherProfileUseCase
import com.example.infoquizapp.domain.teacher.usecases.UpdateTeacherProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TeacherProfileUiState {
    object Idle: TeacherProfileUiState()
    object Loading: TeacherProfileUiState()
    data class Success(val teacher: TeacherProfile): TeacherProfileUiState()
    data class Error(val message: String): TeacherProfileUiState()
}

sealed class TeacherEditUiState {
    object Idle: TeacherEditUiState()
    object Loading: TeacherEditUiState()
    data class Success(val teacher: TeacherProfile): TeacherEditUiState()
    data class Error(val message: String): TeacherEditUiState()
}

class TeacherProfileViewModel(
    private val loadProfile: GetTeacherProfileUseCase,
    private val updateProfile: UpdateTeacherProfileUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<TeacherProfileUiState>(TeacherProfileUiState.Idle)
    val uiState: StateFlow<TeacherProfileUiState> = _uiState

    private val _editState = MutableStateFlow<TeacherEditUiState>(TeacherEditUiState.Idle)
    val editState: StateFlow<TeacherEditUiState> = _editState

    fun load(token: String) = viewModelScope.launch {
        _uiState.value = TeacherProfileUiState.Loading
        val res = loadProfile(token)
        if (res.error != null) _uiState.value = TeacherProfileUiState.Error(res.error)
        else (res.teacherProfile as? Response.Succes)?.let {
            _uiState.value = TeacherProfileUiState.Success(it.result)
        }
    }

    fun save(token: String, update: TeacherUpdate) = viewModelScope.launch {
        _editState.value = TeacherEditUiState.Loading
        val res = updateProfile(token, update)
        if (res.error != null) _editState.value = TeacherEditUiState.Error(res.error)
        else {
            val prof = (res.profile as Response.Succes).result
            _editState.value = TeacherEditUiState.Success(prof)
            load(token)
        }
    }

    fun resetEdit() { _editState.value = TeacherEditUiState.Idle }
}