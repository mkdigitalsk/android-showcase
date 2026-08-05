package sk.mkdigital.androidshowcase.domain.useCase

import sk.mkdigital.androidshowcase.domain.model.User
import sk.mkdigital.androidshowcase.domain.repository.UserRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<None, List<User>>() {
    override suspend fun run(params: None): List<User> = userRepository.getUsers()
}
