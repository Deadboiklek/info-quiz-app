package com.example.infoquizapp.domain.quest.usecases

import com.example.infoquizapp.data.quest.model.QuestOut

data class AllQuestsResult(
    val quests: List<QuestOut>?,
    val error: String?
)
