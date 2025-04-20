package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.model.TeacherUpdate
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class UpdateTeacherProfileUseCase(
    private val repo: TeacherRepository
) {
    suspend operator fun invoke(token: String, u: TeacherUpdate): TeacherProfileUpdateResult =
        when (val r = repo.updateProfile(token, u)) {
            is Response.Succes -> TeacherProfileUpdateResult(profile = r, error = null)
            is Response.Error  -> TeacherProfileUpdateResult(profile = null, error = r.error.message)
        }
}