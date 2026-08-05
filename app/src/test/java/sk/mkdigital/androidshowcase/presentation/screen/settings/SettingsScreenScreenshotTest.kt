package sk.mkdigital.androidshowcase.presentation.screen.settings

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class SettingsScreenScreenshotTest(
    stateHolder: StateHolder<SettingsState>,
    mode: NightMode,
) : BaseScreenshotTest<SettingsState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(SettingsScreenPreviewParams())
    }

    @Test
    fun settingsScreen() {
        screenshot {
            SettingsScreen(state = state)
        }
    }
}
