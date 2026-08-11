package sk.mkdigital.androidshowcase.domain.useCase.crash

import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.data.crash.CrashReporter
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.base.test
import org.junit.jupiter.api.Test

class RecordExceptionUseCaseTest : BaseTest<RecordExceptionUseCase>() {

    override lateinit var classUnderTest: RecordExceptionUseCase

    @MockK
    private lateinit var crashReporter: CrashReporter

    override fun beforeEach() {
        classUnderTest = RecordExceptionUseCase(crashReporter)
    }

    @Test
    fun `invoke calls recordException on the crash reporter`() = runTest {
        val exception = RuntimeException("Test exception")

        test(
            given = {
                coJustRun { crashReporter.recordException(exception) }
            },
            whenAction = {
                classUnderTest(exception)
            },
            then = {
                coVerify(exactly = 1) { crashReporter.recordException(exception) }
            }
        )
    }
}
