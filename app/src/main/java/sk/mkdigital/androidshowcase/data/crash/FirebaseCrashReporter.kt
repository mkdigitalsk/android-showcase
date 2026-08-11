package sk.mkdigital.androidshowcase.data.crash

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCrashReporter @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : CrashReporter {

    override fun recordException(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        firebaseCrashlytics.log(message)
    }
}
