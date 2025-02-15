package com.example.infoquizapp.domain.quest.usecases

import com.example.infoquizapp.data.quest.network.Response
import com.example.infoquizapp.domain.quest.repository.QuestRepository

class GetUserQuestsUseCase(private val repository: QuestRepository) {
    suspend operator fun invoke(token: String): UserQuestsResult {
        return when (val response = repository.getUserQuests(token)) {
            is Response.Success -> UserQuestsResult(response.result, null)
            is Response.Error -> UserQuestsResult(null, response.error.message)
        }
    }
}