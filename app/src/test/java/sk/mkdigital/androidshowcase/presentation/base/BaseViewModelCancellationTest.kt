package sk.mkdigital.androidshowcase.presentation.base

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.domain.exceptions.base.BaseException
import sk.mkdigital.androidshowcase.fake.NoOpLogger

@ExperimentalCoroutinesApi
class BaseViewModelCancellationTest : BaseViewModelTest<BaseViewModelCancellationTest.TestViewModel>() {

    override lateinit var classUnderTest: TestViewModel

    data class TestState(val value: Int = 0)

    class TestViewModel : BaseViewModel<TestState>(TestState(), logsScreenName = false) {
        val started = CompletableDeferred<Unit>()
        var errorReported: BaseException? = null
        var succeeded = false

        fun runForever() = execute(
            action = {
                started.complete(Unit)
                CompletableDeferred<Unit>().await()
            },
            onSuccess = { succeeded = true },
            onError = { errorReported = it },
        )

        fun boom() = execute(
            action = { error("boom") },
            onError = { errorReported = it },
        )
    }

    override fun beforeEach() {
        classUnderTest = TestViewModel().apply { logger = NoOpLogger }
    }

    // Cancelling a ViewModel job unwinds it; the user sees no failure.
    @Test
    fun `cancelling an operation reports neither success nor error`() = runTest {
        val job = classUnderTest.runForever()
        classUnderTest.started.await()
        job.cancel()

        assertTrue(job.isCancelled)
        assertFalse(classUnderTest.succeeded)
        assertFalse(classUnderTest.errorReported != null)
    }

    @Test
    fun `a real failure still reports an error`() = runTest {
        classUnderTest.boom().join()

        assertNotNull(classUnderTest.errorReported)
    }
}
