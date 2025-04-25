package com.example.infoquizapp.presentation.game.view

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.infoquizapp.presentation.game.viewmodel.GameViewModel

@Composable
fun SpaceshipControlArea(
    viewModel: GameViewModel,
    screenWidth: Float,
    screenHeight: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.moveSpaceship(dragAmount, screenWidth, screenHeight)
                }
            }
    )
}