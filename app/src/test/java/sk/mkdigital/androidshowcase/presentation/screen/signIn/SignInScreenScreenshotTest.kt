package sk.mkdigital.androidshowcase.presentation.screen.signIn

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class SignInScreenScreenshotTest(
    stateHolder: StateHolder<SignInUiState>,
    mode: NightMode,
) : BaseScreenshotTest<SignInUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(SignInScreenPreviewParams())
    }

    @Test
    fun signInScreen() {
        screenshot {
            SignInScreen(state = state)
        }
    }
}
