package sk.mkdigital.androidshowcase.presentation.screen.storage

import dagger.hilt.android.lifecycle.HiltViewModel
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.domain.useCase.storage.ClearCacheUseCase
import sk.mkdigital.androidshowcase.domain.useCase.storage.LoadStorageDataUseCase
import sk.mkdigital.androidshowcase.domain.useCase.storage.ObserveStorageDataUseCase
import sk.mkdigital.androidshowcase.domain.useCase.storage.SetPersistentCounterUseCase
import sk.mkdigital.androidshowcase.domain.useCase.storage.SetSessionCounterUseCase
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val loadStorageDataUseCase: LoadStorageDataUseCase,
    private val observeStorageDataUseCase: ObserveStorageDataUseCase,
    private val setSessionCounterUseCase: SetSessionCounterUseCase,
    private val setPersistentCounterUseCase: SetPersistentCounterUseCase,
    private val clearCacheUseCase: ClearCacheUseCase
) : BaseViewModel<StorageUiState>(StorageUiState()) {

    override fun loadInitialData() {
        observe(
            onStart = { loadStorageDataUseCase() },
            flow = observeStorageDataUseCase(),
            onEach = { data ->
                newState {
                    it.copy(sessionCounter = data.sessionCounter, persistentCounter = data.persistentCounter)
                }
            }
        )
    }

    fun incrementSessionCounter() {
        val newValue = state.value.sessionCounter + 1
        execute(action = { setSessionCounterUseCase(newValue) })
    }

    fun decrementSessionCounter() {
        val newValue = (state.value.sessionCounter - 1).coerceAtLeast(0)
        execute(action = { setSessionCounterUseCase(newValue) })
    }

    fun incrementPersistentCounter() {
        val newValue = state.value.persistentCounter + 1
        execute(action = { setPersistentCounterUseCase(newValue) })
    }

    fun decrementPersistentCounter() {
        val newValue = (state.value.persistentCounter - 1).coerceAtLeast(0)
        execute(action = { setPersistentCounterUseCase(newValue) })
    }

    fun clearSession() {
        execute(action = { clearCacheUseCase() })
    }
}

data class StorageUiState(
    val sessionCounter: Int = 0,
    val persistentCounter: Int = 0
)
