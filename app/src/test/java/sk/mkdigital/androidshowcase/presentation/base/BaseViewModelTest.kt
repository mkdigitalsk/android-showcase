package sk.mkdigital.androidshowcase.presentation.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import sk.mkdigital.androidshowcase.base.BaseTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest<ClassUnderTest> : BaseTest<ClassUnderTest>() {

    @BeforeEach
    fun setUpMainDispatcher() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterEach
    fun tearDownMainDispatcher() {
        Dispatchers.resetMain()
    }
}
