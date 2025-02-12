package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.model.UserOut

data class ProfileResult(
    val profile: UserOut?,
    val error: String?
)
