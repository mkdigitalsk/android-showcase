package sk.mkdigital.androidshowcase.presentation.screen.database

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.domain.model.Note
import sk.mkdigital.androidshowcase.domain.model.NoteSortOption
import sk.mkdigital.androidshowcase.presentation.base.lifecycleAwareViewModel
import sk.mkdigital.androidshowcase.presentation.component.AppSearchField
import sk.mkdigital.androidshowcase.presentation.component.AppTextField
import sk.mkdigital.androidshowcase.presentation.component.ErrorView
import sk.mkdigital.androidshowcase.presentation.component.LoadingView
import sk.mkdigital.androidshowcase.presentation.component.buttons.ContainedButton
import sk.mkdigital.androidshowcase.presentation.component.buttons.OutlinedButton
import sk.mkdigital.androidshowcase.presentation.component.cards.AppElevatedCard
import sk.mkdigital.androidshowcase.presentation.component.image.AppIconNeutral80
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer2
import sk.mkdigital.androidshowcase.presentation.component.text.bodyLarge.TextBodyLargeNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.bodySmall.TextBodySmallNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.labelMedium.TextLabelMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.titleLarge.TextTitleLargeNeutral80
import sk.mkdigital.androidshowcase.presentation.foundation.AppTheme
import sk.mkdigital.androidshowcase.presentation.foundation.floatingNavBarSpace
import sk.mkdigital.androidshowcase.presentation.foundation.space4

@Composable
fun DatabaseScreen(viewModel: DatabaseViewModel = lifecycleAwareViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DatabaseScreen(
        state = state,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onSortOptionChange = viewModel::onSortOptionChanged,
        onToggleFilterMenu = viewModel::toggleFilterMenu,
        onDismissFilterMenu = viewModel::dismissFilterMenu,
        onTitleChange = viewModel::onTitleChanged,
        onContentChange = viewModel::onContentChanged,
        onAddNote = viewModel::addNote,
        onDeleteNote = viewModel::deleteNote,
        onDeleteAllNotes = viewModel::deleteAllNotes
    )
}

@Composable
internal fun DatabaseScreen(
    state: DatabaseUiState,
    onSearchQueryChange: (String) -> Unit = {},
    onSortOptionChange: (NoteSortOption) -> Unit = {},
    onToggleFilterMenu: () -> Unit = {},
    onDismissFilterMenu: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String) -> Unit = {},
    onAddNote: () -> Unit = {},
    onDeleteNote: (Long) -> Unit = {},
    onDeleteAllNotes: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = space4,
            end = space4,
            top = space4,
            bottom = floatingNavBarSpace
        ),
        verticalArrangement = Arrangement.spacedBy(space4)
    ) {
        item {
            TextBodyLargeNeutral80(stringResource(R.string.database_subtitle))
        }

        item {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = onSearchQueryChange,
                sortOption = state.sortOption,
                onSortOptionChange = onSortOptionChange,
                showFilterMenu = state.showFilterMenu,
                onToggleFilterMenu = onToggleFilterMenu,
                onDismissFilterMenu = onDismissFilterMenu
            )
        }

        item {
            AddNoteCard(
                title = state.newNoteTitle,
                content = state.newNoteContent,
                onTitleChange = onTitleChange,
                onContentChange = onContentChange,
                onAddClick = onAddNote
            )
        }

        if (state.error && state.notes.isEmpty()) {
            item {
                ErrorView(message = stringResource(R.string.database_error))
            }
        } else if (state.isLoading && state.notes.isEmpty()) {
            item {
                LoadingView()
            }
        } else if (state.notes.isEmpty()) {
            item {
                TextBodyMediumNeutral80(stringResource(R.string.database_empty))
            }
        }

        items(items = state.notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                onDeleteClick = { onDeleteNote(note.id) }
            )
        }

        if (state.notes.isNotEmpty()) {
            item {
                OutlinedButton(
                    text = stringResource(R.string.database_clear_all),
                    onClick = onDeleteAllNotes
                )
            }
        }
    }
}

@Composable
@Suppress("LongParameterList")
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    sortOption: NoteSortOption,
    onSortOptionChange: (NoteSortOption) -> Unit,
    showFilterMenu: Boolean,
    onToggleFilterMenu: () -> Unit,
    onDismissFilterMenu: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space4)
    ) {
        AppSearchField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(R.string.database_search_hint)
        )

        SortMenu(
            sortOption = sortOption,
            onSortOptionChange = onSortOptionChange,
            expanded = showFilterMenu,
            onToggleFilterMenu = onToggleFilterMenu,
            onDismissFilterMenu = onDismissFilterMenu
        )
    }
}

@Composable
private fun SortMenu(
    sortOption: NoteSortOption,
    onSortOptionChange: (NoteSortOption) -> Unit,
    expanded: Boolean,
    onToggleFilterMenu: () -> Unit,
    onDismissFilterMenu: () -> Unit,
) {
    Box {
        IconButton(onClick = onToggleFilterMenu) {
            AppIconNeutral80(
                imageVector = Icons.Filled.FilterList,
                contentDescription = stringResource(R.string.database_filter)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissFilterMenu
        ) {
            TextLabelMediumNeutral80(
                text = stringResource(R.string.database_sort_by),
                modifier = Modifier.padding(horizontal = space4, vertical = space4)
            )
            SortMenuItem(
                label = stringResource(R.string.database_sort_date_newest),
                selected = sortOption == NoteSortOption.DATE_DESC,
                onClick = { onSortOptionChange(NoteSortOption.DATE_DESC) }
            )
            SortMenuItem(
                label = stringResource(R.string.database_sort_date_oldest),
                selected = sortOption == NoteSortOption.DATE_ASC,
                onClick = { onSortOptionChange(NoteSortOption.DATE_ASC) }
            )
            SortMenuItem(
                label = stringResource(R.string.database_sort_title_asc),
                selected = sortOption == NoteSortOption.TITLE_ASC,
                onClick = { onSortOptionChange(NoteSortOption.TITLE_ASC) }
            )
            SortMenuItem(
                label = stringResource(R.string.database_sort_title_desc),
                selected = sortOption == NoteSortOption.TITLE_DESC,
                onClick = { onSortOptionChange(NoteSortOption.TITLE_DESC) }
            )
        }
    }
}

@Composable
private fun SortMenuItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { TextBodyMediumNeutral80(label) },
        onClick = onClick,
        leadingIcon = if (selected) {
            { TextBodyMediumNeutral80("✓") }
        } else null
    )
}

@Composable
private fun AddNoteCard(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onAddClick: () -> Unit,
) {
    AppElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space4)
    ) {
        AppTextField(
            value = title,
            onValueChange = onTitleChange,
            label = stringResource(R.string.database_title_label),
            placeholder = stringResource(R.string.database_title_hint),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer2()
        AppTextField(
            value = content,
            onValueChange = onContentChange,
            label = stringResource(R.string.database_content_label),
            placeholder = stringResource(R.string.database_content_hint),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer2()
        ContainedButton(
            text = stringResource(R.string.database_add_note),
            onClick = onAddClick,
            enabled = title.isNotBlank()
        )
    }
}

@Composable
private fun NoteCard(
    note: NoteUiModel,
    onDeleteClick: () -> Unit,
) {
    AppElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space4)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TextTitleLargeNeutral80(note.title)
                if (note.content.isNotBlank()) {
                    Spacer2()
                    TextBodyMediumNeutral80(note.content)
                }
                Spacer2()
                TextBodySmallNeutral80(note.createdAt)
            }
            IconButton(onClick = onDeleteClick) {
                AppIconNeutral80(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.database_delete)
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DatabaseScreenPreview(
    @PreviewParameter(DatabaseScreenPreviewParams::class) state: DatabaseUiState
) {
    AppTheme {
        DatabaseScreen(state = state)
    }
}

internal class DatabaseScreenPreviewParams : PreviewParameterProvider<DatabaseUiState> {
    override val values = sequenceOf(
        DatabaseUiState(isLoading = true),
        DatabaseUiState(error = true),
        DatabaseUiState(
            notes = listOf(
                Note(id = 1, title = "title", content = "content", createdAt = 0).toUiModel(),
                Note(
                    id = 2, title = "title2", content = "content2", createdAt = 1769344378
                ).toUiModel(),
            ),
            newNoteTitle = "New Note",
            newNoteContent = "Content",
            sortOption = NoteSortOption.DATE_ASC,
            showFilterMenu = true
        )
    )
}
