package sk.mkdigital.androidshowcase.util

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.coroutines.cancellation.CancellationException

class SuspendRunCatchingTest {

    @Test
    fun `cancellation propagates instead of becoming a failed result`() = runTest {
        assertThrows<CancellationException> {
            suspendRunCatching { throw CancellationException("cancelled") }
        }
    }

    @Test
    fun `any other throwable becomes a failed result`() = runTest {
        val result = suspendRunCatching { throw IllegalStateException("boom") }

        assertTrue(result.isFailure)
        assertEquals("boom", result.exceptionOrNull()?.message)
    }

    @Test
    fun `a value becomes a successful result`() = runTest {
        assertEquals("ok", suspendRunCatching { "ok" }.getOrNull())
    }
}
