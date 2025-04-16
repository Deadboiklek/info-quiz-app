package com.example.infoquizapp.data.quest.repository

import com.example.infoquizapp.data.quest.model.CompleteQuestResponse
import com.example.infoquizapp.data.quest.model.QuestOut
import com.example.infoquizapp.data.quest.network.ApiQuestsService
import com.example.infoquizapp.data.quest.network.Response
import com.example.infoquizapp.domain.quest.repository.QuestRepository

class QuestRepositoryImpl(private val apiQuestsService: ApiQuestsService) : QuestRepository {
    override suspend fun getAllQuests(): Response<List<QuestOut>> {
        return apiQuestsService.getQuests()
    }

    override suspend fun getUserQuests(token: String): Response<List<QuestOut>> {
        return apiQuestsService.getUserQuests(token)
    }

    override suspend fun completeQuest(questId: Int, token: String): Response<CompleteQuestResponse> {
        return apiQuestsService.completeQuest(questId, token)
    }
}