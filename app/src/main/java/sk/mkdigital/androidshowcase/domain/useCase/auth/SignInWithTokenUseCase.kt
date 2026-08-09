package sk.mkdigital.androidshowcase.domain.useCase.auth

import sk.mkdigital.androidshowcase.domain.model.AuthSession
import sk.mkdigital.androidshowcase.domain.repository.AuthRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class SignInWithTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<None, AuthSession?>() {
    override suspend fun run(params: None): AuthSession? = authRepository.signInWithToken()
}
