package sk.mkdigital.androidshowcase.presentation.screen.register

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class RegisterScreenScreenshotTest(
    stateHolder: StateHolder<RegisterUiState>,
    mode: NightMode,
) : BaseScreenshotTest<RegisterUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(RegisterScreenPreviewParams())
    }

    @Test
    fun registerScreen() {
        screenshot {
            RegisterScreen(state = state)
        }
    }
}
