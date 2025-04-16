package com.example.infoquizapp.presentation.quest.util

import com.example.infoquizapp.R

fun getQuestImageRes(imageName: String) : Int {
    return when (imageName) {
        "quest_done1" -> R.drawable.quest_done1
        "quest_done2" -> R.drawable.quest_done2
        "quest_done3" -> R.drawable.quest_done3
        "quest_octopus" -> R.drawable.quest_octopus
        "quest_flying" -> R.drawable.quest_flying
        else -> R.drawable.question_mark
    }
}