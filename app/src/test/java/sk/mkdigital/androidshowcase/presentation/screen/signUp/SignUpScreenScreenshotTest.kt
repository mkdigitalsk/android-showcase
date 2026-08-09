package sk.mkdigital.androidshowcase.presentation.screen.signUp

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class SignUpScreenScreenshotTest(
    stateHolder: StateHolder<SignUpUiState>,
    mode: NightMode,
) : BaseScreenshotTest<SignUpUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(SignUpScreenPreviewParams())
    }

    @Test
    fun signUpScreen() {
        screenshot {
            SignUpScreen(state = state)
        }
    }
}
