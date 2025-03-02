package com.example.infoquizapp.domain.practice.usecases

import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.domain.practice.repository.PracticeRepository

class GetAllPracticeUseCase(private val repository: PracticeRepository) {
    suspend operator fun invoke(): List<PracticeEntity> {
        return repository.getAllPractice()
    }
}