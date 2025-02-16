package com.example.infoquizapp.domain.quest.usecases

import com.example.infoquizapp.data.quest.model.CompleteQuestResponse

data class CompleteQuestResult(
    val response: CompleteQuestResponse?,
    val error: String?
)
