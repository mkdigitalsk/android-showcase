package com.mk.androidshowcase.domain.useCase.auth

import com.mk.androidshowcase.domain.model.AuthSession
import com.mk.androidshowcase.domain.repository.AuthRepository
import com.mk.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<LoginUseCase.Params, AuthSession>() {

    data class Params(val email: String, val password: String)

    override suspend fun run(params: Params): AuthSession =
        authRepository.login(params.email, params.password)
}
