package com.example.infoquizapp.domain.theory.usecases

import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.repository.TheoryRepository

class GetTheoryUseCase(private val repository: TheoryRepository) {
    suspend operator fun invoke(id: Int): TheoryEntity? {
        return repository.getTheory(id)
    }
}