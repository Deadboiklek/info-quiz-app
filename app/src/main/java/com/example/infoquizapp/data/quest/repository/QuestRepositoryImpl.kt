package com.example.infoquizapp.data.quest.repository

import com.example.infoquizapp.data.quest.model.CompleteQuestResponse
import com.example.infoquizapp.data.quest.model.QuestOut
import com.example.infoquizapp.data.quest.network.ApiQuestService
import com.example.infoquizapp.data.quest.network.Response
import com.example.infoquizapp.domain.quest.repository.QuestRepository

class QuestRepositoryImpl(private val apiQuestService: ApiQuestService) : QuestRepository {
    override suspend fun getUserQuests(token: String): Response<List<QuestOut>> {
        return apiQuestService.getUserQuests(token)
    }

    override suspend fun completeQuest(questId: Int, token: String): Response<CompleteQuestResponse> {
        return apiQuestService.completeQuest(questId, token)
    }
}