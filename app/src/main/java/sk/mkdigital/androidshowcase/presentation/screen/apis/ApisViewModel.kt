package sk.mkdigital.androidshowcase.presentation.screen.apis

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.domain.useCase.biometric.AuthenticateWithBiometricUseCase
import sk.mkdigital.androidshowcase.domain.useCase.biometric.IsBiometricEnabledUseCase
import sk.mkdigital.androidshowcase.domain.useCase.location.GetLastKnownLocationUseCase
import sk.mkdigital.androidshowcase.domain.useCase.location.ObserveLocationUpdatesUseCase
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModel
import sk.mkdigital.androidshowcase.presentation.base.NavEvent
import javax.inject.Inject

@HiltViewModel
class ApisViewModel @Inject constructor(
    private val isBiometricEnabledUseCase: IsBiometricEnabledUseCase,
    private val authenticateWithBiometricUseCase: AuthenticateWithBiometricUseCase,
    private val getLastKnownLocationUseCase: GetLastKnownLocationUseCase,
    private val observeLocationUpdatesUseCase: ObserveLocationUpdatesUseCase,
) : BaseViewModel<ApisUiState>(ApisUiState()) {

    private var locationUpdatesJob: Job? = null

    override fun loadInitialData() {
        execute(
            action = { isBiometricEnabledUseCase() },
            onSuccess = { enabled -> newState { it.copy(biometricsAvailable = enabled) } }
        )
    }

    fun share() {
        navigate(
            ApisNavEvent.Share(
                textRes = R.string.platform_apis_demo_share_text,
                titleRes = R.string.platform_apis_demo_share_title,
                urlRes = R.string.platform_apis_demo_url
            )
        )
    }

    fun dial() {
        navigate(ApisNavEvent.Dial(R.string.platform_apis_demo_phone))
    }

    fun openLink() {
        navigate(ApisNavEvent.OpenLink(R.string.platform_apis_demo_url))
    }

    fun sendEmail() {
        navigate(
            ApisNavEvent.SendEmail(
                R.string.platform_apis_demo_email,
                R.string.platform_apis_demo_email_subject,
                R.string.platform_apis_demo_email_body
            )
        )
    }

    fun copyToClipboard() {
        navigate(ApisNavEvent.CopyToClipboard(R.string.platform_apis_demo_copy_text))
        newState { it.copy(copiedToClipboard = true) }
    }

    fun getLocation() {
        execute(
            action = { getLastKnownLocationUseCase() },
            onLoading = { newState { it.copy(locationLoading = true, locationError = false) } },
            onSuccess = { location ->
                newState { it.copy(location = location.toUiModel(), locationLoading = false) }
            },
            onError = {
                newState { it.copy(locationLoading = false, locationError = true) }
            }
        )
    }

    override fun onResume() {
        requireState { state -> if (state.shouldTrackLocation) startLocationUpdates() }
    }

    override fun onPause() {
        requireState { currentState -> newState { it.copy(shouldTrackLocation = currentState.isTrackingLocation) } }
        stopLocationUpdates()
    }

    fun startLocationUpdates() {
        if (locationUpdatesJob?.isActive == true) return
        newState { it.copy(isTrackingLocation = true, locationUpdatesError = false) }
        locationUpdatesJob = observe(
            flow = observeLocationUpdatesUseCase(ObserveLocationUpdatesUseCase.Params(highAccuracy = true)),
            onEach = { location -> newState { it.copy(trackedLocation = location.toUiModel()) } },
            onError = { newState { it.copy(isTrackingLocation = false, locationUpdatesError = true) } }
        )
    }

    fun stopLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = null
        newState { it.copy(isTrackingLocation = false) }
    }

    fun authenticateWithBiometrics() {
        execute(
            action = { authenticateWithBiometricUseCase() },
            onLoading = { newState { it.copy(biometricsLoading = true, biometricsResult = null) } },
            onSuccess = { result ->
                newState { it.copy(biometricsLoading = false, biometricsResult = result.toUiModel()) }
            },
            onError = { error ->
                val result = BiometricUiModel(BiometricUiStatus.FAILED, error.message?.takeIf { it.isNotBlank() })
                newState { it.copy(biometricsLoading = false, biometricsResult = result) }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}

@Immutable
data class ApisUiState(
    val copiedToClipboard: Boolean = false,
    val location: LocationUiModel? = null,
    val locationLoading: Boolean = false,
    val locationError: Boolean = false,
    val isTrackingLocation: Boolean = false,
    val shouldTrackLocation: Boolean = false,
    val trackedLocation: LocationUiModel? = null,
    val locationUpdatesError: Boolean = false,
    val biometricsAvailable: Boolean = false,
    val biometricsLoading: Boolean = false,
    val biometricsResult: BiometricUiModel? = null,
)

sealed interface ApisNavEvent : NavEvent {
    data class Share(
        @param:StringRes val textRes: Int,
        @param:StringRes val titleRes: Int,
        @param:StringRes val urlRes: Int
    ) : ApisNavEvent
    data class Dial(@param:StringRes val numberRes: Int) : ApisNavEvent
    data class OpenLink(@param:StringRes val urlRes: Int) : ApisNavEvent
    data class SendEmail(
        @param:StringRes val toRes: Int,
        @param:StringRes val subjectRes: Int,
        @param:StringRes val bodyRes: Int
    ) : ApisNavEvent
    data class CopyToClipboard(@param:StringRes val textRes: Int) : ApisNavEvent
}
