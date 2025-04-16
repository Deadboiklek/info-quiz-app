package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.model.UserStatistics
import com.example.infoquizapp.data.profile.network.Response

data class StatisticsResult(
    val statistics: Response<UserStatistics>?,
    val error: String?
)
