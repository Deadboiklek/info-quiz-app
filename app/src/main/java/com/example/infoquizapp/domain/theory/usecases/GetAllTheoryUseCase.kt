package com.example.infoquizapp.domain.theory.usecases

import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.repository.TheoryRepository

class GetAllTheoryUseCase(private val repository: TheoryRepository) {
    suspend operator fun invoke(): List<TheoryEntity> {
        return repository.getAllTheory()
    }
}