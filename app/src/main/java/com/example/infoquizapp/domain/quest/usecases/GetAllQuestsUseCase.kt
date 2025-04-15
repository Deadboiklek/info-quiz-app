package com.example.infoquizapp.domain.quest.usecases

import com.example.infoquizapp.data.quest.network.Response
import com.example.infoquizapp.domain.quest.repository.QuestRepository

class GetAllQuestsUseCase(private val repository: QuestRepository) {
    suspend operator fun invoke(): AllQuestsResult {
        return when (val response = repository.getAllQuests()) {
            is Response.Success -> AllQuestsResult(response.result, null)
            is Response.Error -> AllQuestsResult(null, response.error.message)
        }
    }
}