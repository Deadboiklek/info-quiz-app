package com.example.infoquizapp.presentation.theory.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.infoquizapp.presentation.theory.viewmodel.AllTheoryUiState
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryViewModel


@Composable
fun TheoryScreen(
    viewModel: TheoryViewModel,
    onTheoryCardClick: (Int) -> Unit
) {

    val uiState by viewModel.combinedState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllTheory()
    }

    Scaffold { paddingValues ->
        when (uiState.allTheoryState) {
            AllTheoryUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AllTheoryUiState.Success -> {
                val theories = (uiState.allTheoryState as AllTheoryUiState.Success).theories

                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(theories) { theory ->
                        TheoryCard(theory = theory, onTheoryContentScreen = onTheoryCardClick)
                    }
                }
            }

            is AllTheoryUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState.allTheoryState as AllTheoryUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            AllTheoryUiState.Idle -> { /* ничего не делаем */ }
        }
    }
}