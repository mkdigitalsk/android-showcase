package sk.mkdigital.androidshowcase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.mkdigital.androidshowcase.data.repository.BiometricRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.DateRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.LocationRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.SettingsRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.AuthRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.database.NoteRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.storage.StorageRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.user.UserRepositoryImpl
import sk.mkdigital.androidshowcase.data.notification.NotificationRepositoryImpl
import sk.mkdigital.androidshowcase.data.notification.PushNotificationServiceImpl
import sk.mkdigital.androidshowcase.domain.repository.AuthRepository
import sk.mkdigital.androidshowcase.domain.repository.BiometricRepository
import sk.mkdigital.androidshowcase.domain.repository.DateRepository
import sk.mkdigital.androidshowcase.domain.repository.LocationRepository
import sk.mkdigital.androidshowcase.domain.repository.NoteRepository
import sk.mkdigital.androidshowcase.domain.repository.NotificationRepository
import sk.mkdigital.androidshowcase.domain.repository.PushNotificationService
import sk.mkdigital.androidshowcase.domain.repository.SettingsRepository
import sk.mkdigital.androidshowcase.domain.repository.StorageRepository
import sk.mkdigital.androidshowcase.data.repository.note.RemoteNoteRepositoryImpl
import sk.mkdigital.androidshowcase.domain.repository.RemoteNoteRepository
import sk.mkdigital.androidshowcase.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("TooManyFunctions")
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindRemoteNoteRepository(impl: RemoteNoteRepositoryImpl): RemoteNoteRepository

    @Binds
    @Singleton
    fun bindStorageRepository(impl: StorageRepositoryImpl): StorageRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    fun bindBiometricRepository(impl: BiometricRepositoryImpl): BiometricRepository

    @Binds
    @Singleton
    fun bindDateRepository(impl: DateRepositoryImpl): DateRepository

    @Binds
    @Singleton
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    fun bindPushNotificationService(impl: PushNotificationServiceImpl): PushNotificationService
}
