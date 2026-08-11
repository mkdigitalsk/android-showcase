package sk.mkdigital.androidshowcase.domain.useCase.auth

import sk.mkdigital.androidshowcase.domain.repository.UserRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<None, Unit>() {
    override suspend fun run(params: None) = userRepository.deleteAccount()
}
