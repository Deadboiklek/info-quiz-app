package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.model.TeacherProfile
import com.example.infoquizapp.data.teacher.network.Response

data class TeacherProfileResult(
    val teacherProfile: Response<TeacherProfile>?,
    val error: String?
)
