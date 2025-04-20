package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.model.UserOut

data class ProfileUpdateResult(val profile: UserOut?, val error: String?)
