package sk.mkdigital.androidshowcase.presentation.screen.notifications

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class NotificationsScreenScreenshotTest(
    stateHolder: StateHolder<NotificationsUiState>,
    mode: NightMode,
) : BaseScreenshotTest<NotificationsUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(NotificationsScreenPreviewParams())
    }

    @Test
    fun notificationsScreen() {
        screenshot {
            NotificationsScreen(state = state)
        }
    }
}
