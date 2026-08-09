package sk.mkdigital.androidshowcase.data.network

import kotlinx.serialization.SerializationException
import sk.mkdigital.androidshowcase.domain.exceptions.base.ApiException
import sk.mkdigital.androidshowcase.domain.exceptions.base.DataErrorCode
import sk.mkdigital.androidshowcase.domain.exceptions.base.DataException
import sk.mkdigital.androidshowcase.domain.exceptions.base.NetworkErrorCode
import sk.mkdigital.androidshowcase.domain.exceptions.base.NetworkException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> handleApiCall(
    crossinline call: suspend () -> T
): T = try {
    call()
} catch (e: SocketTimeoutException) {
    throw NetworkException(
        message = "Request timeout: ${e.message}",
        cause = e,
        userMessage = "Request timed out. Please try again.",
        errorCode = NetworkErrorCode.TIMEOUT
    )
} catch (e: UnknownHostException) {
    throw NetworkException(
        message = "No network connection: ${e.message}",
        cause = e,
        errorCode = NetworkErrorCode.NO_CONNECTION
    )
} catch (e: java.io.IOException) {
    throw NetworkException(
        message = "Network error: ${e.message}",
        cause = e,
        errorCode = NetworkErrorCode.NO_CONNECTION
    )
} catch (e: HttpException) {
    val code = e.code()
    throw ApiException(
        httpCode = code,
        message = "HTTP error: ${e.message()}",
        cause = e,
        userMessage = when (code) {
            401 -> "Please sign in again."
            403 -> "You don't have permission to access this."
            404 -> "The requested resource was not found."
            in 500..599 -> "Server error. Please try again later."
            else -> "Request failed. Please try again."
        }
    )
} catch (e: SerializationException) {
    throw DataException(
        message = "Serialization error: ${e.message}",
        cause = e,
        errorCode = DataErrorCode.SERIALIZATION
    )
}
