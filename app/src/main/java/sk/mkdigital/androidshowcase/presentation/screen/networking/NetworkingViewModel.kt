package sk.mkdigital.androidshowcase.presentation.screen.networking

import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.mkdigital.androidshowcase.domain.useCase.GetUsersUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkingViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<NetworkingUiState>(NetworkingUiState()) {

    override fun loadInitialData() {
        fetchUsers()
    }

    fun fetchUsers() {
        execute(
            action = { getUsersUseCase() },
            onLoading = { newState { it.copy(isLoading = true, error = null) } },
            onSuccess = { users ->
                newState { it.copy(isLoading = false, users = users.map { user -> user.toUiModel() }) }
            },
            onError = { error -> newState { it.copy(isLoading = false, error = error.message) } }
        )
    }

    fun refresh() {
        fetchUsers()
    }
}

@Immutable
data class NetworkingUiState(
    val isLoading: Boolean = false,
    val users: List<UserUiModel> = emptyList(),
    val error: String? = null
)
