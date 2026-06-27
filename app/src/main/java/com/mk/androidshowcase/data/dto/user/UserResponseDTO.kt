package com.mk.androidshowcase.data.dto.user

import com.mk.androidshowcase.data.base.TransformToDomainModel
import com.mk.androidshowcase.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val id: Long,
    val email: String,
    val name: String,
    val createdAt: Long,
    val themeMode: ThemeModeDTO,
    val locale: String,
) : TransformToDomainModel<User> {
    override fun transform(): User = User(id = id, email = email, name = name)
}
