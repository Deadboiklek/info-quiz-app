package com.example.infoquizapp.presentation.quest.viewmodel

import com.example.infoquizapp.data.quest.model.QuestOut

data class QuestsUiModel(
    val quest: QuestOut,
    val isCompleted: Boolean
)
