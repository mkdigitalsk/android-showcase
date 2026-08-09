package sk.mkdigital.androidshowcase.data.dto.user

import sk.mkdigital.androidshowcase.data.base.TransformToDomainModel
import sk.mkdigital.androidshowcase.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val id: Long,
    val email: String,
    val createdAt: Long,
    val themeMode: ThemeModeDTO,
    val locale: String,
) : TransformToDomainModel<User> {
    override fun transform(): User = User(id = id, email = email)
}
