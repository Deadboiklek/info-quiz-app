package com.example.infoquizapp.presentation.practice.viewmodel

data class CombinedPracticeUiState(
    val practiceState: PracticeUiState = PracticeUiState.Idle,
    val allPracticeUiState: AllPracticeUiState = AllPracticeUiState.Idle
)
