package sk.mkdigital.androidshowcase.presentation.screen.apis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.presentation.base.CollectNavEvents
import sk.mkdigital.androidshowcase.presentation.base.NavEvent
import sk.mkdigital.androidshowcase.presentation.base.lifecycleAwareViewModel
import sk.mkdigital.androidshowcase.presentation.base.router.CopyRouter
import sk.mkdigital.androidshowcase.presentation.base.router.DialRouter
import sk.mkdigital.androidshowcase.presentation.base.router.EmailRouter
import sk.mkdigital.androidshowcase.presentation.base.router.LinkRouter
import sk.mkdigital.androidshowcase.presentation.base.router.ShareRouter
import sk.mkdigital.androidshowcase.presentation.component.buttons.OutlinedButton
import sk.mkdigital.androidshowcase.presentation.component.cards.AppElevatedCard
import sk.mkdigital.androidshowcase.presentation.component.permission.rememberLocationPermissionState
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer2
import sk.mkdigital.androidshowcase.presentation.component.text.bodyLarge.TextBodyLargeNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.foundation.floatingNavBarSpace
import sk.mkdigital.androidshowcase.presentation.foundation.space4

private enum class PendingLocationAction { NONE, GET_LOCATION, START_UPDATES }

@Composable
private fun rememberLocationActionHandler(
    onGetLocation: () -> Unit,
    onStartUpdates: () -> Unit
): (PendingLocationAction) -> Unit {
    val locationPermission = rememberLocationPermissionState()
    var pendingAction by remember { mutableStateOf(PendingLocationAction.NONE) }
    val currentOnGetLocation by rememberUpdatedState(onGetLocation)
    val currentOnStartUpdates by rememberUpdatedState(onStartUpdates)

    LaunchedEffect(locationPermission.isGranted, pendingAction) {
        if (locationPermission.isGranted && pendingAction != PendingLocationAction.NONE) {
            when (pendingAction) {
                PendingLocationAction.GET_LOCATION -> currentOnGetLocation()
                PendingLocationAction.START_UPDATES -> currentOnStartUpdates()
                PendingLocationAction.NONE -> Unit
            }
            pendingAction = PendingLocationAction.NONE
        }
    }

    return { action ->
        if (locationPermission.isGranted) {
            when (action) {
                PendingLocationAction.GET_LOCATION -> currentOnGetLocation()
                PendingLocationAction.START_UPDATES -> currentOnStartUpdates()
                PendingLocationAction.NONE -> Unit
            }
        } else {
            pendingAction = action
            locationPermission.requestPermission()
        }
    }
}

@Suppress("CyclomaticComplexMethod", "CognitiveComplexMethod")
@Composable
fun ApisScreen(
    shareRouter: ShareRouter,
    dialRouter: DialRouter,
    linkRouter: LinkRouter,
    emailRouter: EmailRouter,
    copyRouter: CopyRouter,
    viewModel: ApisViewModel = lifecycleAwareViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val handleLocationAction = rememberLocationActionHandler(
        onGetLocation = viewModel::getLocation,
        onStartUpdates = viewModel::startLocationUpdates
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = space4,
            end = space4,
            top = space4,
            bottom = floatingNavBarSpace
        ),
        verticalArrangement = Arrangement.spacedBy(space4)
    ) {
        item {
            Column {
                TextBodyLargeNeutral80(stringResource(R.string.platform_apis_subtitle))
            }
        }

        item {
            ApiCard(
                icon = Icons.Outlined.Share,
                title = stringResource(R.string.platform_apis_share_title)
            ) {
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_share_action),
                    onClick = viewModel::share
                )
            }
        }

        item {
            ApiCard(
                icon = Icons.Outlined.Phone,
                title = stringResource(R.string.platform_apis_dial_title)
            ) {
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_dial_action),
                    onClick = viewModel::dial
                )
            }
        }

        item {
            ApiCard(
                icon = Icons.Outlined.Link,
                title = stringResource(R.string.platform_apis_link_title)
            ) {
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_link_action),
                    onClick = viewModel::openLink
                )
            }
        }

        item {
            ApiCard(
                icon = Icons.Outlined.Email,
                title = stringResource(R.string.platform_apis_email_title)
            ) {
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_email_action),
                    onClick = viewModel::sendEmail
                )
            }
        }

        item {
            ApiCard(
                icon = Icons.Outlined.ContentCopy,
                title = stringResource(R.string.platform_apis_copy_title)
            ) {
                if (state.copiedToClipboard) {
                    TextBodyMediumNeutral80(stringResource(R.string.platform_apis_copied_message))
                    Spacer2()
                }
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_copy_action),
                    onClick = viewModel::copyToClipboard
                )
            }
        }

        item {
            val loadingText = stringResource(R.string.platform_apis_location_loading)
            val errorText = stringResource(R.string.platform_apis_location_error)
            val location = state.location
            ApiCard(
                icon = Icons.Outlined.LocationOn,
                title = stringResource(R.string.platform_apis_location_title)
            ) {
                val locationText = when {
                    state.locationLoading -> loadingText
                    state.locationError -> errorText
                    location != null ->
                        stringResource(R.string.platform_apis_location_result, location.latitude, location.longitude)
                    else -> null
                }
                locationText?.let {
                    TextBodyMediumNeutral80(it)
                    Spacer2()
                }
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_location_action),
                    onClick = { handleLocationAction(PendingLocationAction.GET_LOCATION) }
                )
            }
        }

        item {
            val errorText = stringResource(R.string.platform_apis_location_updates_error)
            val trackedLocation = state.trackedLocation
            ApiCard(
                icon = Icons.Outlined.MyLocation,
                title = stringResource(R.string.platform_apis_location_updates_title)
            ) {
                val trackedText = when {
                    state.locationUpdatesError -> errorText
                    trackedLocation != null -> stringResource(
                        R.string.platform_apis_location_result,
                        trackedLocation.latitude,
                        trackedLocation.longitude
                    )
                    else -> null
                }
                trackedText?.let {
                    TextBodyMediumNeutral80(it)
                    Spacer2()
                }
                ApiCardButton(
                    text = stringResource(
                        if (state.isTrackingLocation) R.string.platform_apis_location_updates_stop
                        else R.string.platform_apis_location_updates_start
                    ),
                    onClick = {
                        if (state.isTrackingLocation) viewModel.stopLocationUpdates()
                        else handleLocationAction(PendingLocationAction.START_UPDATES)
                    }
                )
            }
        }

        item {
            val successText = stringResource(R.string.platform_apis_biometrics_success)
            val failedText = stringResource(R.string.platform_apis_biometrics_failed)
            val cancelledText = stringResource(R.string.platform_apis_biometrics_cancelled)
            val notAvailableText = stringResource(R.string.platform_apis_biometrics_not_available)
            val activityNotAvailableText = stringResource(R.string.platform_apis_biometrics_activity_not_available)
            val unknownErrorText = stringResource(R.string.platform_apis_biometrics_unknown_error)
            val biometric = state.biometricsResult
            ApiCard(
                icon = Icons.Outlined.Fingerprint,
                title = stringResource(R.string.platform_apis_biometrics_title)
            ) {
                val biometricText = when {
                    !state.biometricsAvailable -> notAvailableText
                    state.biometricsLoading -> "..."
                    biometric != null -> when (biometric.status) {
                        BiometricUiStatus.SUCCESS -> successText
                        BiometricUiStatus.FAILED -> "$failedText: ${biometric.errorDetail ?: unknownErrorText}"
                        BiometricUiStatus.CANCELLED -> cancelledText
                        BiometricUiStatus.NOT_AVAILABLE -> notAvailableText
                        BiometricUiStatus.ACTIVITY_NOT_AVAILABLE -> activityNotAvailableText
                    }
                    else -> null
                }
                biometricText?.let {
                    TextBodyMediumNeutral80(it)
                    Spacer2()
                }
                ApiCardButton(
                    text = stringResource(R.string.platform_apis_biometrics_action),
                    onClick = viewModel::authenticateWithBiometrics,
                    enabled = state.biometricsAvailable && !state.biometricsLoading
                )
            }
        }
    }

    ApisNavEvents(
        shareRouter = shareRouter,
        dialRouter = dialRouter,
        linkRouter = linkRouter,
        emailRouter = emailRouter,
        copyRouter = copyRouter,
        navEvent = viewModel.navEvent,
    )
}

@Composable
private fun ApiCard(
    icon: ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    AppElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space4)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(space4))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                content = {
                    TextBodyLargeNeutral80(title)
                    Spacer2()
                    content()
                }
            )
        }
    }
}

@Composable
private fun ApiCardButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        text = text,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    )
}

@Composable
@Suppress("LongParameterList")
private fun ApisNavEvents(
    shareRouter: ShareRouter,
    dialRouter: DialRouter,
    linkRouter: LinkRouter,
    emailRouter: EmailRouter,
    copyRouter: CopyRouter,
    navEvent: SharedFlow<NavEvent>,
) {
    val context = LocalContext.current
    CollectNavEvents(navEventFlow = navEvent) { event ->
        when (event) {
            is ApisNavEvent.Share -> shareRouter.share(
                text = context.getString(event.textRes),
                title = context.getString(event.titleRes),
                url = context.getString(event.urlRes)
            )
            is ApisNavEvent.Dial -> dialRouter.dial(context.getString(event.numberRes))
            is ApisNavEvent.OpenLink -> linkRouter.openLink(context.getString(event.urlRes))
            is ApisNavEvent.SendEmail -> emailRouter.sendEmail(
                context.getString(event.toRes),
                context.getString(event.subjectRes),
                context.getString(event.bodyRes)
            )
            is ApisNavEvent.CopyToClipboard -> copyRouter.copyToClipboard(context.getString(event.textRes))
        }
    }
}
