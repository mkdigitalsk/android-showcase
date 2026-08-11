package sk.mkdigital.androidshowcase.domain.useCase.crash

import sk.mkdigital.androidshowcase.data.crash.CrashReporter
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class RecordExceptionUseCase @Inject constructor(
    private val crashReporter: CrashReporter
) : UseCase<Throwable, Unit>() {
    override suspend fun run(params: Throwable) = crashReporter.recordException(params)
}
