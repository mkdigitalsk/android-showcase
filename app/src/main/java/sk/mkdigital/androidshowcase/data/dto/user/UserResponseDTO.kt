package sk.mkdigital.androidshowcase.data.dto.user

import sk.mkdigital.androidshowcase.data.base.TransformToDomainModel
import sk.mkdigital.androidshowcase.data.dto.LanguageDTO
import sk.mkdigital.androidshowcase.domain.model.User
import kotlinx.serialization.Serializable

/** @property language defaulted, so a newer server never fails the read. */
@Serializable
data class UserResponseDTO(
    val id: Long,
    val email: String,
    val createdAt: Long,
    val themeMode: ThemeModeDTO,
    val language: LanguageDTO = LanguageDTO.DEFAULT,
    val demo: Boolean = false,
) : TransformToDomainModel<User> {
    override fun transform(): User = User(id = id, email = email, isDemo = demo)
}
