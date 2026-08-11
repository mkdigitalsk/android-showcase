package sk.mkdigital.androidshowcase.data.repository.user

import sk.mkdigital.androidshowcase.data.base.transformAll
import sk.mkdigital.androidshowcase.domain.model.User
import sk.mkdigital.androidshowcase.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val client: UserClient
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        return client.fetchUsers().transformAll()
    }

    override suspend fun deleteAccount() = client.deleteMe()
}
