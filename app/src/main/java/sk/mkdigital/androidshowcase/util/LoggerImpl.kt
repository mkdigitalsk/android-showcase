package sk.mkdigital.androidshowcase.util

import android.util.Log
import sk.mkdigital.androidshowcase.data.analytics.AnalyticsClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggerImpl @Inject constructor(
    private val analyticsClient: AnalyticsClient
) : Logger {

    override fun e(log: String) {
        Log.e(TAG, log)
    }

    override fun e(e: Throwable) {
        Log.e(TAG, e.stackTraceToString())
        analyticsClient.recordException(e)
    }

    override fun e(log: String, e: Throwable) {
        Log.e(TAG, log, e)
        analyticsClient.recordException(e)
    }

    override fun d(log: String) {
        Log.d(TAG, log)
    }

    private companion object {
        private const val TAG = "Logger"
    }
}
