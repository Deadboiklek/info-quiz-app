package com.example.infoquizapp.domain.theory.repository

import com.example.infoquizapp.data.theory.TheoryEntity

interface TheoryRepository {
    suspend fun getTheory(id: Int): TheoryEntity?
    suspend fun markTheoryAsRead(id: Int)
}