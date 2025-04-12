package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.model.StudentInfo

data class GetTeacherStudentsResult(
    val students: List<StudentInfo>? = null,
    val error: String?
)
