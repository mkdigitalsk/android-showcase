package sk.mkdigital.androidshowcase.presentation.screen.database

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class DatabaseScreenScreenshotTest(
    stateHolder: StateHolder<DatabaseUiState>,
    mode: NightMode,
) : BaseScreenshotTest<DatabaseUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(DatabaseScreenPreviewParams())
    }

    @Test
    fun databaseScreen() {
        screenshot {
            DatabaseScreen(state = state)
        }
    }
}
