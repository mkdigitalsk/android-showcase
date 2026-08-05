package sk.mkdigital.androidshowcase.presentation.screen.login

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class LoginScreenScreenshotTest(
    stateHolder: StateHolder<LoginUiState>,
    mode: NightMode,
) : BaseScreenshotTest<LoginUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(LoginScreenPreviewParams())
    }

    @Test
    fun loginScreen() {
        screenshot {
            LoginScreen(state = state)
        }
    }
}
