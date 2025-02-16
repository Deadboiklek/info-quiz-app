package com.example.infoquizapp.domain.quest.repository

import com.example.infoquizapp.data.quest.model.CompleteQuestResponse
import com.example.infoquizapp.data.quest.model.QuestOut
import com.example.infoquizapp.data.quest.network.Response

interface QuestRepository {
    suspend fun getUserQuests(token: String): Response<List<QuestOut>>
    suspend fun completeQuest(questId: Int, token: String): Response<CompleteQuestResponse>
}