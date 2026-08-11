package sk.mkdigital.androidshowcase.presentation.screen.settings

import app.cash.paparazzi.DeviceConfig
import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class SettingsScreenScreenshotTest(
    stateHolder: StateHolder<SettingsState>,
    mode: NightMode,
) : BaseScreenshotTest<SettingsState>(stateHolder, mode, SCREEN_REACHING_THE_ACCOUNT_ACTIONS) {

    companion object {
        // Sign Out and the delete row sit below the version footer, past a Pixel 5's fold: at that
        // height the picture is the same whether the row is a button, a sentence or nothing at all.
        private val SCREEN_REACHING_THE_ACCOUNT_ACTIONS = DeviceConfig.PIXEL_5.copy(screenHeight = 3000)

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
