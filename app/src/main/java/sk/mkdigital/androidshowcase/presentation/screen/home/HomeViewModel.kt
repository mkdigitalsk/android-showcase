package sk.mkdigital.androidshowcase.presentation.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModel
import sk.mkdigital.androidshowcase.presentation.base.NavEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeUiState>(HomeUiState()) {

    fun onFeatureClick(featureId: FeatureId) {
        navigate(HomeNavEvent.ToFeature(featureId))
    }
}

data class HomeUiState(
    val features: List<Feature> = showcaseFeatures
)

sealed interface HomeNavEvent : NavEvent {
    data class ToFeature(val featureId: FeatureId) : HomeNavEvent
}
