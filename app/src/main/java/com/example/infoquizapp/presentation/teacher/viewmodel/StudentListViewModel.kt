package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.teacher.model.StudentInfo
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherStudentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentListViewModel(private val getTeacherStudentsUseCase: GetTeacherStudentsUseCase) : ViewModel() {

    private val _studentsState = MutableStateFlow<List<StudentInfo>>(emptyList())
    val studentsState: StateFlow<List<StudentInfo>> = _studentsState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun loadStudents(token: String) {
        viewModelScope.launch {
            val result = getTeacherStudentsUseCase(token)
            result.students?.let {
                _studentsState.value = it
                _errorState.value = null
            } ?: run {
                _errorState.value = result.error
            }
        }
    }
}