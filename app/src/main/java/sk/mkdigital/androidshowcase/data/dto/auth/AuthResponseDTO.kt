package sk.mkdigital.androidshowcase.data.dto.auth

import sk.mkdigital.androidshowcase.data.dto.LanguageDTO
import sk.mkdigital.androidshowcase.data.dto.user.ThemeModeDTO
import kotlinx.serialization.Serializable

/** @property language defaulted, so a newer server never fails the sign-in. */
@Serializable
data class AuthUserDTO(
    val id: Long,
    val email: String,
    val themeMode: ThemeModeDTO,
    val language: LanguageDTO = LanguageDTO.DEFAULT,
)

@Serializable
data class AuthResponseDTO(
    val token: String,
    val user: AuthUserDTO,
)
