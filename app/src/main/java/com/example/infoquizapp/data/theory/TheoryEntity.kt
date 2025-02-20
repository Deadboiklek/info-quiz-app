package com.example.infoquizapp.data.theory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theory")
data class TheoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String
)
