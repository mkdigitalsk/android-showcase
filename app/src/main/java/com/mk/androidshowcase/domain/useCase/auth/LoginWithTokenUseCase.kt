package com.mk.androidshowcase.domain.useCase.auth

import com.mk.androidshowcase.domain.model.AuthSession
import com.mk.androidshowcase.domain.repository.AuthRepository
import com.mk.androidshowcase.domain.useCase.base.None
import com.mk.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class LoginWithTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<None, AuthSession?>() {
    override suspend fun run(params: None): AuthSession? = authRepository.loginWithToken()
}
