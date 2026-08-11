package sk.mkdigital.androidshowcase.domain.repository

import sk.mkdigital.androidshowcase.domain.model.User

interface UserRepository {

    suspend fun getUsers(): List<User>

    suspend fun deleteAccount()

}
