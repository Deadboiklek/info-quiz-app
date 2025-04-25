package com.example.infoquizapp.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.model.UserUpdate
import com.example.infoquizapp.domain.profile.usecases.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val user: UserOut) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class ProfileEditUiState {
    object Idle : ProfileEditUiState()
    object Loading : ProfileEditUiState()
    data class Success(val user: UserOut) : ProfileEditUiState()
    data class Error(val message: String) : ProfileEditUiState()
}

class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val updateProfile: UpdateProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _editState = MutableStateFlow<ProfileEditUiState>(ProfileEditUiState.Idle)
    val editState: StateFlow<ProfileEditUiState> = _editState

    fun loadProfile(token: String) = viewModelScope.launch {
        _uiState.value = ProfileUiState.Loading
        val result = getProfile(token)
        _uiState.value = if (result.error != null)
            ProfileUiState.Error(result.error)
        else
            ProfileUiState.Success((result.profile as Response.Succes).result)
    }

    fun saveChanges(token: String, update: UserUpdate) = viewModelScope.launch {
        _editState.value = ProfileEditUiState.Loading
        val res = updateProfile(token, update)
        if (res.error != null) {
            _editState.value = ProfileEditUiState.Error(res.error)
        } else {
            _editState.value = ProfileEditUiState.Success(res.profile!!)
            // можно перезагрузить основный профиль
            loadProfile(token)
        }
    }

    fun resetEditState() {
        _editState.value = ProfileEditUiState.Idle
    }
}