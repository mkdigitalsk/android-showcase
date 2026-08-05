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
import sk.mkdigital.androidshowcase.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindStorageRepository(impl: StorageRepositoryImpl): StorageRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindBiometricRepository(impl: BiometricRepositoryImpl): BiometricRepository

    @Binds
    @Singleton
    abstract fun bindDateRepository(impl: DateRepositoryImpl): DateRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindPushNotificationService(impl: PushNotificationServiceImpl): PushNotificationService
}
