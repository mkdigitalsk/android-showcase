package sk.mkdigital.androidshowcase.data.crash

interface CrashReporter {
    fun recordException(throwable: Throwable)
    fun log(message: String)
}
