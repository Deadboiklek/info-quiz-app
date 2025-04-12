package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.model.StudentStatistics

data class GetStudentStatisticsResult(
    val statistics: StudentStatistics? = null,
    val error: String?
)
