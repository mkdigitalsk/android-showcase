package sk.mkdigital.androidshowcase.data.repository.user

import sk.mkdigital.androidshowcase.data.dto.user.UserResponseDTO
import sk.mkdigital.androidshowcase.data.network.UserApi
import sk.mkdigital.androidshowcase.data.network.handleApiCall
import javax.inject.Inject

interface UserClient {
    suspend fun fetchUser(id: Long): UserResponseDTO
    suspend fun fetchUsers(): List<UserResponseDTO>
}

class UserClientImpl @Inject constructor(
    private val userApi: UserApi
) : UserClient {

    override suspend fun fetchUser(id: Long): UserResponseDTO = handleApiCall {
        userApi.fetchUser(id)
    }

    override suspend fun fetchUsers(): List<UserResponseDTO> = handleApiCall {
        userApi.fetchUsers()
    }
}
