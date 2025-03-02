package com.example.infoquizapp.domain.practice.repository

import com.example.infoquizapp.data.practice.PracticeEntity

interface PracticeRepository {
    suspend fun getPractice(id: Int): PracticeEntity?
    suspend fun markPracticeAsDone(id: Int)
    suspend fun getAllPractice(): List<PracticeEntity>
}