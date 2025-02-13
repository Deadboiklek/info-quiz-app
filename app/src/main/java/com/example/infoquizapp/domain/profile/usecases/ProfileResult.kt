package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.network.Response

data class ProfileResult(
    val profile: Response<UserOut>?,
    val error: String?
)
