package com.example.infoquizapp.domain.quest.usecases

import com.example.infoquizapp.data.quest.network.Response
import com.example.infoquizapp.domain.quest.repository.QuestRepository

class CompleteQuestUseCase(private val repository: QuestRepository) {
    suspend operator fun invoke(questId: Int, token: String): CompleteQuestResult {
        return when (val response = repository.completeQuest(questId, token)) {
            is Response.Success -> CompleteQuestResult(response.result, null)
            is Response.Error -> CompleteQuestResult(null, response.error.message)
        }
    }
}