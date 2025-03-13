package com.example.infoquizapp.data.theory.repository

import com.example.infoquizapp.data.TheoryDao
import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.repository.TheoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheoryRepositoryImpl(private val theoryDao: TheoryDao) : TheoryRepository {
    override suspend fun getTheory(id: Int): TheoryEntity? {
        return theoryDao.getTheoryById(id)
    }

    override suspend fun markTheoryAsRead(id: Int) {
        theoryDao.updateReadStatus(id, true)
    }

    override suspend fun getAllTheory(): List<TheoryEntity> {
        return theoryDao.getAllTheory()
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (theoryDao.getTheoryCount() == 0) {
                val initialData = listOf(
                    TheoryEntity(
                        title = "Введение в программирование",
                        content = "Основные концепции программирования...",
                        isRead = false
                    ),
                    TheoryEntity(
                        title = "Основы Kotlin",
                        content = "Синтаксис и базовые конструкции языка...",
                        isRead = false
                    )
                )
                theoryDao.insertAllTheory(initialData)
            }
        }
    }
}