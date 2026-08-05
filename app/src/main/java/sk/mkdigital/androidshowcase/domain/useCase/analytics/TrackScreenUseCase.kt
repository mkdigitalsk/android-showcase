package sk.mkdigital.androidshowcase.domain.useCase.analytics

import sk.mkdigital.androidshowcase.data.analytics.AnalyticsClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackScreenUseCase @Inject constructor(
    private val analyticsClient: AnalyticsClient
) {
    operator fun invoke(screenName: String) {
        analyticsClient.trackScreen(screenName)
    }
}
