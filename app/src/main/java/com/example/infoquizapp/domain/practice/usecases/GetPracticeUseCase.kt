package com.example.infoquizapp.domain.practice.usecases

import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.domain.practice.repository.PracticeRepository

class GetPracticeUseCase(private val repository: PracticeRepository) {
    suspend operator fun invoke(id: Int): PracticeEntity? {
        return repository.getPractice(id)
    }
}