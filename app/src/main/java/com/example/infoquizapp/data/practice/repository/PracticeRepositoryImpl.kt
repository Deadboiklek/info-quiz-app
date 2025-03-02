package com.example.infoquizapp.data.practice.repository

import com.example.infoquizapp.data.practice.PracticeDao
import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.domain.practice.repository.PracticeRepository

class PracticeRepositoryImpl(private val practiceDao: PracticeDao) : PracticeRepository {

    override suspend fun getPractice(id: Int): PracticeEntity? {
        return practiceDao.getPracticeById(id)
    }

    override suspend fun markPracticeAsDone(id: Int) {
        return practiceDao.updatePracticeStatus(id, true)
    }

    override suspend fun getAllPractice(): List<PracticeEntity> {
        return practiceDao.getAllPractice()
    }
}