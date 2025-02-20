package com.example.infoquizapp.data.theory.repository

import com.example.infoquizapp.data.theory.TheoryDao
import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.repository.TheoryRepository

class TheoryRepositoryImpl(private val theoryDao: TheoryDao) : TheoryRepository {
    override suspend fun getTheory(id: Int): TheoryEntity? {
        return theoryDao.getTheoryById(id)
    }
}