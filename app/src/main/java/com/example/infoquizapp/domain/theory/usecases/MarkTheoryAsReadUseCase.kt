package com.example.infoquizapp.domain.theory.usecases

import com.example.infoquizapp.domain.theory.repository.TheoryRepository

class MarkTheoryAsReadUseCase(private val repository: TheoryRepository) {
    suspend operator fun invoke(id: Int) {
        repository.markTheoryAsRead(id)
    }
}