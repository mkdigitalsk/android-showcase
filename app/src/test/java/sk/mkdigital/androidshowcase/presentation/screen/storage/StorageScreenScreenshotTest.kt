package sk.mkdigital.androidshowcase.presentation.screen.storage

import com.android.resources.NightMode
import sk.mkdigital.androidshowcase.presentation.base.BaseScreenshotTest
import sk.mkdigital.androidshowcase.presentation.base.StateHolder
import sk.mkdigital.androidshowcase.presentation.base.generateParameterizedData
import org.junit.Test
import org.junit.runners.Parameterized

class StorageScreenScreenshotTest(
    stateHolder: StateHolder<StorageUiState>,
    mode: NightMode,
) : BaseScreenshotTest<StorageUiState>(stateHolder, mode) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<*> = generateParameterizedData(StorageScreenPreviewParams())
    }

    @Test
    fun storageScreen() {
        screenshot {
            StorageScreen(state = state)
        }
    }
}
