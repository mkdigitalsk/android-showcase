package com.mk.androidshowcase.presentation.screen.database

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.mk.androidshowcase.domain.model.NoteSortOption
import com.mk.androidshowcase.domain.useCase.notes.DeleteAllNotesUseCase
import com.mk.androidshowcase.domain.useCase.notes.DeleteNoteUseCase
import com.mk.androidshowcase.domain.useCase.notes.InsertNoteUseCase
import com.mk.androidshowcase.domain.useCase.notes.SearchNotesUseCase
import com.mk.androidshowcase.fake.NoOpLogger
import com.mk.androidshowcase.presentation.base.BaseViewModelTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DatabaseViewModelTest : BaseViewModelTest<DatabaseViewModel>() {

    override lateinit var classUnderTest: DatabaseViewModel

    private val searchNotesUseCase = mockk<SearchNotesUseCase>()
    private val insertNoteUseCase = mockk<InsertNoteUseCase>()
    private val deleteNoteUseCase = mockk<DeleteNoteUseCase>()
    private val deleteAllNotesUseCase = mockk<DeleteAllNotesUseCase>()

    override fun beforeEach() {
        classUnderTest = DatabaseViewModel(
            searchNotesUseCase = searchNotesUseCase,
            insertNoteUseCase = insertNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase,
            deleteAllNotesUseCase = deleteAllNotesUseCase,
        ).apply { logger = NoOpLogger }
    }

    @Test
    fun `default state has empty notes list`() {
        assertTrue(classUnderTest.state.value.notes.isEmpty())
    }

    @Test
    fun `default state has loading true`() {
        assertTrue(classUnderTest.state.value.isLoading)
    }

    @Test
    fun `default state has no error`() {
        assertFalse(classUnderTest.state.value.error)
    }

    @Test
    fun `default state has empty search query`() {
        assertEquals("", classUnderTest.state.value.searchQuery)
    }

    @Test
    fun `default state has DATE_DESC sort option`() {
        assertEquals(NoteSortOption.DATE_DESC, classUnderTest.state.value.sortOption)
    }

    @Test
    fun `default state has filter menu hidden`() {
        assertFalse(classUnderTest.state.value.showFilterMenu)
    }

    @Test
    fun `default state has empty new note fields`() {
        val state = classUnderTest.state.value
        assertEquals("", state.newNoteTitle)
        assertEquals("", state.newNoteContent)
    }

    @Test
    fun `onSearchQueryChanged updates search query`() {
        classUnderTest.onSearchQueryChanged("test query")

        assertEquals("test query", classUnderTest.state.value.searchQuery)
    }

    @Test
    fun `onSortOptionChanged updates sort option`() {
        classUnderTest.onSortOptionChanged(NoteSortOption.TITLE_ASC)

        assertEquals(NoteSortOption.TITLE_ASC, classUnderTest.state.value.sortOption)
    }

    @Test
    fun `onSortOptionChanged hides filter menu`() {
        classUnderTest.toggleFilterMenu() // Open menu first

        classUnderTest.onSortOptionChanged(NoteSortOption.DATE_ASC)

        assertFalse(classUnderTest.state.value.showFilterMenu)
    }

    @Test
    fun `toggleFilterMenu opens closed menu`() {
        classUnderTest.toggleFilterMenu()

        assertTrue(classUnderTest.state.value.showFilterMenu)
    }

    @Test
    fun `toggleFilterMenu closes open menu`() {
        classUnderTest.toggleFilterMenu() // Open

        classUnderTest.toggleFilterMenu() // Close

        assertFalse(classUnderTest.state.value.showFilterMenu)
    }

    @Test
    fun `dismissFilterMenu closes menu`() {
        classUnderTest.toggleFilterMenu() // Open

        classUnderTest.dismissFilterMenu()

        assertFalse(classUnderTest.state.value.showFilterMenu)
    }

    @Test
    fun `onTitleChanged updates new note title`() {
        classUnderTest.onTitleChanged("New Title")

        assertEquals("New Title", classUnderTest.state.value.newNoteTitle)
    }

    @Test
    fun `onContentChanged updates new note content`() {
        classUnderTest.onContentChanged("New Content")

        assertEquals("New Content", classUnderTest.state.value.newNoteContent)
    }

    @Test
    fun `addNote with blank title does not insert`() {
        classUnderTest.onTitleChanged("   ") // Blank title
        classUnderTest.onContentChanged("Content")

        classUnderTest.addNote()

        coVerify(exactly = 0) { insertNoteUseCase(any()) }
    }

    @Test
    fun `addNote with empty title does not insert`() {
        classUnderTest.onContentChanged("Content")

        classUnderTest.addNote()

        coVerify(exactly = 0) { insertNoteUseCase(any()) }
    }

    @Test
    fun `DatabaseUiState default values are correct`() {
        val state = DatabaseUiState()
        assertTrue(state.notes.isEmpty())
        assertTrue(state.isLoading)
        assertFalse(state.error)
        assertEquals("", state.newNoteTitle)
        assertEquals("", state.newNoteContent)
        assertEquals("", state.searchQuery)
        assertEquals(NoteSortOption.DATE_DESC, state.sortOption)
        assertFalse(state.showFilterMenu)
    }

    @Test
    fun `DatabaseUiState can hold notes`() {
        val notes = listOf(
            NoteUiModel(id = 1, title = "Test Note", content = "Test Content", createdAt = "2009-02-13 23:31"),
            NoteUiModel(id = 2, title = "Test Note", content = "Test Content", createdAt = "2009-02-13 23:31"),
        )
        val state = DatabaseUiState(notes = notes)
        assertEquals(2, state.notes.size)
    }

    @Test
    fun `DatabaseUiState can have error state`() {
        val state = DatabaseUiState(error = true)
        assertTrue(state.error)
    }

    @Test
    fun `NoteSortOption has DATE_DESC value`() {
        assertEquals(NoteSortOption.DATE_DESC, NoteSortOption.valueOf("DATE_DESC"))
    }

    @Test
    fun `NoteSortOption has DATE_ASC value`() {
        assertEquals(NoteSortOption.DATE_ASC, NoteSortOption.valueOf("DATE_ASC"))
    }

    @Test
    fun `NoteSortOption has TITLE_ASC value`() {
        assertEquals(NoteSortOption.TITLE_ASC, NoteSortOption.valueOf("TITLE_ASC"))
    }

    @Test
    fun `NoteSortOption has TITLE_DESC value`() {
        assertEquals(NoteSortOption.TITLE_DESC, NoteSortOption.valueOf("TITLE_DESC"))
    }
}
