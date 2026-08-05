package sk.mkdigital.androidshowcase.data.dto

import sk.mkdigital.androidshowcase.data.dto.auth.AuthResponseDTO
import sk.mkdigital.androidshowcase.domain.model.AuthSession

fun AuthResponseDTO.toAuthSession() = AuthSession(
    token = token,
    userId = user.id,
    email = user.email,
    name = user.name,
)
