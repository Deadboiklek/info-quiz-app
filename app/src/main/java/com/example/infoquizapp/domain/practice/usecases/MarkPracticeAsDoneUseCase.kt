package com.example.infoquizapp.domain.practice.usecases

import com.example.infoquizapp.domain.practice.repository.PracticeRepository

class MarkPracticeAsDoneUseCase(private val repository: PracticeRepository) {
    suspend operator fun invoke(id: Int) {
        repository.markPracticeAsDone(id)
    }
}