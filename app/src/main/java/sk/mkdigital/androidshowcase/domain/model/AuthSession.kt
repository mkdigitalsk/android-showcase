package sk.mkdigital.androidshowcase.domain.model

data class AuthSession(
    val token: String,
    val userId: Long,
    val email: String,
)
