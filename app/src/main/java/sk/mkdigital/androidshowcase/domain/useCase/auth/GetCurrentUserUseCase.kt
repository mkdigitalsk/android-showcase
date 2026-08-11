package sk.mkdigital.androidshowcase.domain.useCase.auth

import sk.mkdigital.androidshowcase.domain.model.User
import sk.mkdigital.androidshowcase.domain.repository.UserRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<None, User>() {
    override suspend fun run(params: None): User = userRepository.getCurrentUser()
}
