package com.example.infoquizapp.presentation.theory.viewmodel

data class CombinedTheoryUiState(
    val theoryState: TheoryUiState = TheoryUiState.Idle,
    val allTheoryState: AllTheoryUiState = AllTheoryUiState.Idle
)
