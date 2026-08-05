package sk.mkdigital.androidshowcase.presentation.screen.home

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class HomeScreenScreenshotTest(
    stateHolder: StateHolder<HomeUiState>,
    mode: NightMode,
) : BaseScreenshotTest<HomeUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(HomeScreenPreviewParams())
    }

    @Test
    fun homeScreen() {
        screenshot {
                HomeScreen(state = state)
        }
    }
}
