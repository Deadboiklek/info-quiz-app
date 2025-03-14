package com.example.infoquizapp.presentation.theory.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryUiState
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryContentScreen(
    viewModel: TheoryViewModel,
    theoryId: Int,
    onBackClick: () -> Unit
) {

    val uiState by viewModel.combinedState.collectAsState()

    var text by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }

    LaunchedEffect(theoryId) {
        viewModel.loadTheory(theoryId)
    }

    when(uiState.theoryState) {
        TheoryUiState.Loading -> CircularProgressIndicator()

        is TheoryUiState.Success -> {

            val theory = (uiState.theoryState as TheoryUiState.Success).theory

            if (text.isEmpty() && theory != null) {
                text = theory.content
                title = theory.title
            }

            Scaffold (
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.background,
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { onBackClick() },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(text = "Назад")
                                }
                                Button(
                                    onClick = {
                                        if (theory != null && !theory.isRead) {
                                            viewModel.markTheoryAsRead(theoryId)
                                            onBackClick()
                                        } else {
                                            onBackClick()
                                        }
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(text = "Закончить")
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    item {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        is TheoryUiState.Error -> {
            Text(
                text = (uiState.theoryState as TheoryUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
        TheoryUiState.Idle -> {  }
    }
}