package sk.mkdigital.androidshowcase.presentation.screen.networking

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.domain.exceptions.NoteConflictException
import sk.mkdigital.androidshowcase.domain.model.RemoteNote
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.note.CreateRemoteNoteUseCase
import sk.mkdigital.androidshowcase.domain.useCase.note.DeleteRemoteNoteUseCase
import sk.mkdigital.androidshowcase.domain.useCase.note.GetRemoteNotesUseCase
import sk.mkdigital.androidshowcase.domain.useCase.note.UpdateRemoteNoteUseCase
import sk.mkdigital.androidshowcase.fake.NoOpLogger
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModelTest

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkingViewModelTest : BaseViewModelTest<NetworkingViewModel>() {

    override lateinit var classUnderTest: NetworkingViewModel

    private val getNotes = mockk<GetRemoteNotesUseCase>()
    private val createNote = mockk<CreateRemoteNoteUseCase>()
    private val updateNote = mockk<UpdateRemoteNoteUseCase>()
    private val deleteNote = mockk<DeleteRemoteNoteUseCase>()

    private val milk = RemoteNote(
        id = 1,
        title = "Buy milk",
        content = "two litres",
        createdAt = 0,
        updatedAt = 0,
        etag = "\"0\"",
    )

    override fun beforeEach() {
        classUnderTest = NetworkingViewModel(getNotes, createNote, updateNote, deleteNote)
            .apply { logger = NoOpLogger }
    }

    @Test
    fun `fetching maps domain notes into UI models`() = runTest {
        coEvery { getNotes(None) } returns listOf(milk)

        classUnderTest.fetchNotes()

        val state = classUnderTest.state.value
        assertFalse(state.isLoading)
        assertEquals(
            listOf(RemoteNoteUiModel(id = 1, title = "Buy milk", content = "two litres", etag = "\"0\"")),
            state.notes,
        )
    }

    @Test
    fun `a failed fetch sets the error and stops loading`() = runTest {
        coEvery { getNotes(None) } throws RuntimeException("boom")

        classUnderTest.fetchNotes()

        val state = classUnderTest.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `a refused write surfaces the server's row rather than an error`() = runTest {
        val theirs = milk.copy(title = "Someone else won", etag = "\"7\"")
        coEvery { updateNote(any()) } throws NoteConflictException(theirs)
        coEvery { getNotes(None) } returns listOf(milk)

        classUnderTest.updateNote(id = 1, title = "Mine", content = "mine", etag = "\"0\"")

        val state = classUnderTest.state.value
        assertEquals("Someone else won", state.conflict?.title)
        assertNull(state.error, "a conflict is someone else saving first, not a failure to report")
    }

    @Test
    fun `keeping mine retries against the tag the server returned`() = runTest {
        val theirs = milk.copy(title = "Someone else won", etag = "\"7\"")
        coEvery {
            updateNote(UpdateRemoteNoteUseCase.Params(1, "Mine", "mine", "\"0\""))
        } throws NoteConflictException(theirs)
        coEvery {
            updateNote(UpdateRemoteNoteUseCase.Params(1, "Mine", "mine", "\"7\""))
        } returns milk.copy(title = "Mine", etag = "\"8\"")
        coEvery { getNotes(None) } returns listOf(milk.copy(title = "Mine", etag = "\"8\""))

        classUnderTest.updateNote(id = 1, title = "Mine", content = "mine", etag = "\"0\"")
        classUnderTest.overwriteConflict(title = "Mine", content = "mine")

        assertNull(classUnderTest.state.value.conflict)
    }
}
