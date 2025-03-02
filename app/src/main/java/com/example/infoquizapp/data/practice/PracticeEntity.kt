package com.example.infoquizapp.data.practice

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "practice")
data class PracticeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false
)