package com.example.infoquizapp.presentation.main.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.io.IOException

sealed class MainUiState {
    object Idle : MainUiState()
    object Loading : MainUiState()
    data class Success(val user: UserOut) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

class MainViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    // UI-state профиля
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Idle)
    val uiState: StateFlow<MainUiState> = _uiState

    // Ключ для DataStore
    private val TUTORIAL_SHOWN = booleanPreferencesKey("tutorial_shown")

    init {
        // Сбрасываем перед каждым запуском приложения
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_SHOWN] = false
            }
        }
    }

    // Flow: надо ли показывать туториал?
    // true  — показывать; false — уже показан
    val showTutorialFlow: StateFlow<Boolean> = dataStore.data
        .catch { e ->
            // если DataStore упал, просто считаем, что надо показать
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
        .map { prefs ->
            // если ещё не было флага или false — показываем
            !(prefs[TUTORIAL_SHOWN] ?: false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    // Помечаем, что туториал больше не показывать
    fun markTutorialShown() {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_SHOWN] = true
            }
        }
    }

    // Загрузка профиля
    fun loadProfile(token: String) {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            val result = getProfileUseCase(token)
            _uiState.value = result.error?.let { MainUiState.Error(it) }
                ?: when (val resp = result.profile) {
                    is Response.Error   -> MainUiState.Error(resp.error.message ?: "Ошибка получения профиля")
                    is Response.Succes  -> MainUiState.Success(resp.result)
                    else                -> MainUiState.Error("Пустой ответ профиля")
                }
        }
    }
}