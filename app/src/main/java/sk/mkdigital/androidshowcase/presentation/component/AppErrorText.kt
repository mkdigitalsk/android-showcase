package sk.mkdigital.androidshowcase.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.presentation.base.AppError

@Composable
@ReadOnlyComposable
fun AppError.text(): String = stringResource(
    when (this) {
        AppError.NO_CONNECTION -> R.string.error_no_connection
        AppError.TIMEOUT -> R.string.error_timeout
        AppError.UNAUTHORIZED -> R.string.error_unauthorized
        AppError.NOT_FOUND -> R.string.error_not_found
        AppError.SERVER -> R.string.error_server
        AppError.DATA -> R.string.error_data
        AppError.LOCATION -> R.string.error_location
        AppError.GENERIC -> R.string.error_generic
    }
)
