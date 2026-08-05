package sk.mkdigital.androidshowcase.domain.useCase.analytics

import sk.mkdigital.androidshowcase.data.analytics.AnalyticsClient
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class RecordExceptionUseCase @Inject constructor(
    private val analyticsClient: AnalyticsClient
) : UseCase<Throwable, Unit>() {
    override suspend fun run(params: Throwable) = analyticsClient.recordException(params)
}
