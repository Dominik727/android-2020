package hu.bme.aut.android.conference.model

import hu.bme.aut.android.conference.enum.userType

data class User(
    val id: Long?,
    val username: String,
    val password: String,
    val email: String,
    val role: userType,
    val phoneNumber: String?,
    val verified: Boolean
)
