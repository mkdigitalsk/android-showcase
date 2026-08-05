package sk.mkdigital.androidshowcase.domain.useCase.auth

import sk.mkdigital.androidshowcase.domain.model.AuthSession
import sk.mkdigital.androidshowcase.domain.repository.AuthRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<RegisterUserUseCase.Params, AuthSession>() {

    data class Params(
        val name: String,
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): AuthSession =
        authRepository.register(params.name, params.email, params.password)
}
