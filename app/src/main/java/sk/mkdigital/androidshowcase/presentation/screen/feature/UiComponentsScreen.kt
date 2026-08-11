package sk.mkdigital.androidshowcase.presentation.screen.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.presentation.component.AppAssistChip
import sk.mkdigital.androidshowcase.presentation.component.AppBadge
import sk.mkdigital.androidshowcase.presentation.component.AppBadgedBox
import sk.mkdigital.androidshowcase.presentation.component.AppBottomSheet
import sk.mkdigital.androidshowcase.presentation.component.AppCheckbox
import sk.mkdigital.androidshowcase.presentation.component.AppConfirmDialog
import sk.mkdigital.androidshowcase.presentation.component.AppDotBadgedBox
import sk.mkdigital.androidshowcase.presentation.component.AppFilterChip
import sk.mkdigital.androidshowcase.presentation.component.AppInputChip
import sk.mkdigital.androidshowcase.presentation.component.AppLinearProgress
import sk.mkdigital.androidshowcase.presentation.component.AppRadioButton
import sk.mkdigital.androidshowcase.presentation.component.AppSlider
import sk.mkdigital.androidshowcase.presentation.component.AppSuggestionChip
import sk.mkdigital.androidshowcase.presentation.component.AppSwitch
import sk.mkdigital.androidshowcase.presentation.component.AppTextField
import sk.mkdigital.androidshowcase.presentation.component.CircularProgress
import sk.mkdigital.androidshowcase.presentation.component.SnackbarType
import sk.mkdigital.androidshowcase.presentation.component.buttons.AppFloatingActionButton
import sk.mkdigital.androidshowcase.presentation.component.buttons.AppSegmentedButton
import sk.mkdigital.androidshowcase.presentation.component.buttons.AppTextButtonPrimary
import sk.mkdigital.androidshowcase.presentation.component.buttons.ContainedButton
import sk.mkdigital.androidshowcase.presentation.component.buttons.OutlinedButton
import sk.mkdigital.androidshowcase.presentation.component.cards.AppCard
import sk.mkdigital.androidshowcase.presentation.component.cards.AppElevatedCard
import sk.mkdigital.androidshowcase.presentation.component.dividers.AppDividerPrimary
import sk.mkdigital.androidshowcase.presentation.component.image.AppIcon
import sk.mkdigital.androidshowcase.presentation.component.showSnackbar
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer2
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer4
import sk.mkdigital.androidshowcase.presentation.component.text.bodyLarge.TextBodyLargeNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.headlineMedium.TextHeadlineMediumPrimary
import sk.mkdigital.androidshowcase.presentation.component.text.titleLarge.TextTitleLargeNeutral80
import sk.mkdigital.androidshowcase.presentation.foundation.floatingNavBarSpace
import sk.mkdigital.androidshowcase.presentation.foundation.space12
import sk.mkdigital.androidshowcase.presentation.foundation.space4
import sk.mkdigital.androidshowcase.presentation.screen.LocalSnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiComponentsScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var checkboxChecked by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }
    var switchChecked by remember { mutableStateOf(false) }
    var selectedRadioOption by remember { mutableStateOf(0) }
    var selectedChips by remember { mutableStateOf(setOf<Int>()) }
    var sliderValue by remember { mutableStateOf(0.5f) }
    var selectedSegment by remember { mutableStateOf(0) }
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    val snackbarMessageDefault = stringResource(R.string.snackbar_message_default)
    val snackbarMessageSuccess = stringResource(R.string.snackbar_message_success)
    val snackbarMessageError = stringResource(R.string.snackbar_message_error)
    val snackbarMessageWarning = stringResource(R.string.snackbar_message_warning)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = space4)
            .padding(top = space4, bottom = floatingNavBarSpace),
        verticalArrangement = Arrangement.spacedBy(space4)
    ) {
        ComponentSection(title = stringResource(R.string.section_buttons)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContainedButton(
                    text = stringResource(R.string.button_contained),
                    onClick = {}
                )
                OutlinedButton(
                    text = stringResource(R.string.button_outlined),
                    onClick = {}
                )
            }
            Spacer2()
            Row(
                horizontalArrangement = Arrangement.spacedBy(space4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTextButtonPrimary(
                    text = stringResource(R.string.button_text),
                    onClick = {}
                )
                AppFloatingActionButton(onClick = {}) {
                    AppIcon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.fab_content_description)
                    )
                }
            }
            Spacer2()
            AppSegmentedButton(
                options = listOf(
                    stringResource(R.string.segment_day),
                    stringResource(R.string.segment_week),
                    stringResource(R.string.segment_month)
                ),
                selectedIndex = selectedSegment,
                onSelectionChanged = { selectedSegment = it },
                modifier = Modifier.fillMaxWidth()
            )

        }

        ComponentSection(title = stringResource(R.string.section_typography)) {
            TextHeadlineMediumPrimary("Headline Medium")
            Spacer2()
            TextTitleLargeNeutral80("Title Large")
            Spacer2()
            TextBodyLargeNeutral80("Body Large")
            Spacer2()
            TextBodyMediumNeutral80("Body Medium")
        }

        ComponentSection(title = stringResource(R.string.section_cards)) {
            AppElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(space4), onClick = {}) {
                TextBodyMediumNeutral80(stringResource(R.string.card_content))
            }
            Spacer2()
            AppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(space4)
            ) {
                TextBodyMediumNeutral80(stringResource(R.string.card_flat_content))
            }
        }

        ComponentSection(title = stringResource(R.string.section_controls)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppCheckbox(
                    checked = checkboxChecked,
                    onCheckedChange = { checkboxChecked = it }
                )
                TextBodyMediumNeutral80(stringResource(R.string.checkbox_label))
            }
        }

        ComponentSection(title = stringResource(R.string.section_text_fields)) {
            AppTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = stringResource(R.string.text_field_label),
                placeholder = stringResource(R.string.text_field_placeholder),
                modifier = Modifier.fillMaxWidth()
            )
        }

        ComponentSection(title = stringResource(R.string.section_switches)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppSwitch(
                    checked = switchChecked,
                    onCheckedChange = { switchChecked = it }
                )
                TextBodyMediumNeutral80(
                    text = stringResource(R.string.switch_label),
                    modifier = Modifier.padding(start = space4)
                )
            }
        }

        ComponentSection(title = stringResource(R.string.section_radio_buttons)) {
            val radioOptions = listOf(
                stringResource(R.string.radio_option_1),
                stringResource(R.string.radio_option_2),
                stringResource(R.string.radio_option_3)
            )
            radioOptions.forEachIndexed { index, label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppRadioButton(
                        selected = selectedRadioOption == index,
                        onClick = { selectedRadioOption = index }
                    )
                    TextBodyMediumNeutral80(label)
                }
            }
        }

        ComponentSection(title = stringResource(R.string.section_chips)) {
            Row(horizontalArrangement = Arrangement.spacedBy(space4)) {
                AppFilterChip(
                    selected = selectedChips.contains(0),
                    onClick = {
                        selectedChips = if (selectedChips.contains(0)) {
                            selectedChips - 0
                        } else {
                            selectedChips + 0
                        }
                    },
                    label = stringResource(R.string.chip_filter)
                )
                AppAssistChip(
                    onClick = {},
                    label = stringResource(R.string.chip_assist)
                )
            }
            Spacer2()
            Row(horizontalArrangement = Arrangement.spacedBy(space4)) {
                AppInputChip(
                    selected = selectedChips.contains(1),
                    onClick = {
                        selectedChips = if (selectedChips.contains(1)) {
                            selectedChips - 1
                        } else {
                            selectedChips + 1
                        }
                    },
                    label = stringResource(R.string.chip_input)
                )
                AppSuggestionChip(
                    onClick = {},
                    label = stringResource(R.string.chip_suggestion)
                )
            }
        }

        ComponentSection(title = stringResource(R.string.section_sliders)) {
            AppSlider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        ComponentSection(title = stringResource(R.string.section_dividers)) {
            TextBodyMediumNeutral80("Content above divider")
            Spacer2()
            AppDividerPrimary()
            Spacer2()
            TextBodyMediumNeutral80("Content below divider")
        }

        ComponentSection(title = stringResource(R.string.section_loading)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space4)
            ) {
                CircularProgress()
                CircularProgress(size = space12)
            }
            Spacer2()
            AppLinearProgress(modifier = Modifier.fillMaxWidth())
        }

        ComponentSection(title = stringResource(R.string.section_badges)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppBadgedBox(count = 5) {
                    AppIcon(imageVector = Icons.Filled.Add)
                }
                AppBadgedBox(count = 123) {
                    AppIcon(imageVector = Icons.Filled.Add)
                }
                AppDotBadgedBox(showBadge = true) {
                    AppIcon(imageVector = Icons.Filled.Add)
                }
                AppBadge()
                AppBadge(count = 7)
            }
        }

        ComponentSection(title = stringResource(R.string.section_images)) {
            AsyncImage(
                model = "https://picsum.photos/400/200",
                contentDescription = stringResource(R.string.image_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        ComponentSection(title = stringResource(R.string.section_snackbar)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space4),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    text = stringResource(R.string.show_snackbar_default),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = snackbarMessageDefault,
                                type = SnackbarType.Default
                            )
                        }
                    }
                )
                OutlinedButton(
                    text = stringResource(R.string.show_snackbar_success),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = snackbarMessageSuccess,
                                type = SnackbarType.Success
                            )
                        }
                    }
                )
            }
            Spacer2()
            Row(
                horizontalArrangement = Arrangement.spacedBy(space4),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    text = stringResource(R.string.show_snackbar_error),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = snackbarMessageError,
                                type = SnackbarType.Error
                            )
                        }
                    }
                )
                OutlinedButton(
                    text = stringResource(R.string.show_snackbar_warning),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = snackbarMessageWarning,
                                type = SnackbarType.Warning
                            )
                        }
                    }
                )
            }
        }

        ComponentSection(title = stringResource(R.string.section_bottom_sheet)) {
            ContainedButton(
                text = stringResource(R.string.show_bottom_sheet),
                onClick = { showBottomSheet = true }
            )
        }

        ComponentSection(title = stringResource(R.string.section_feedback)) {
            ContainedButton(
                text = stringResource(R.string.show_dialog),
                onClick = { showDialog = true }
            )
        }

        Spacer4()
    }

    if (showDialog) {
        AppConfirmDialog(
            title = stringResource(R.string.dialog_title),
            text = stringResource(R.string.dialog_message),
            onDismissRequest = { showDialog = false }
        )
    }

    if (showBottomSheet) {
        AppBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier.padding(space4)
            ) {
                TextHeadlineMediumPrimary(stringResource(R.string.bottom_sheet_title))
                Spacer2()
                TextBodyMediumNeutral80(stringResource(R.string.bottom_sheet_content))
                Spacer4()
                ContainedButton(
                    text = stringResource(R.string.close),
                    onClick = { showBottomSheet = false }
                )
                Spacer4()
            }
        }
    }
}

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextHeadlineMediumPrimary(title)
        Spacer2()
        content()
    }
}
