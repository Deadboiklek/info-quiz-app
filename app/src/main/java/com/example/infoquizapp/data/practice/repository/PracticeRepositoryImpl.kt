package com.example.infoquizapp.data.practice.repository

import com.example.infoquizapp.data.PracticeDao
import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.domain.practice.repository.PracticeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (practiceDao.getPracticeCount() == 0) {
                val initialData = listOf(
                    PracticeEntity(
                        title = "Введение в программирование",
                        description = "Введение в программирование",
                        isDone = false
                    ),
                    PracticeEntity(
                        title = "Основы Kotlin",
                        description = "Основы Kotlin",
                        isDone = false
                    )
                )
                practiceDao.insertAllPractice(initialData)
            }
        }
    }
}