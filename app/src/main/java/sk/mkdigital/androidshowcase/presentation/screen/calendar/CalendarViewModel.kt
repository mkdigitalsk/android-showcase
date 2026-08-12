package sk.mkdigital.androidshowcase.presentation.screen.calendar

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import sk.mkdigital.androidshowcase.domain.model.calendar.DateRange
import sk.mkdigital.androidshowcase.domain.model.calendar.SelectionState
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.domain.useCase.calendar.GetTodayDateUseCase
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModel
import javax.inject.Inject

private const val DAY_INCREMENT = 1

private val SAMPLE_DISABLED_DAY_OFFSETS = setOf(3, 7, 8, 12, 15)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTodayDateUseCase: GetTodayDateUseCase,
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

    override fun loadInitialData() {
        execute(
            action = { getTodayDateUseCase() },
            onSuccess = { today ->
                val disabledDates = generateSampleDisabledDates(today)
                newState { it.copy(today = today, disabledDates = disabledDates) }
            },
        )
    }

    private fun generateSampleDisabledDates(today: LocalDate): Set<LocalDate> =
        SAMPLE_DISABLED_DAY_OFFSETS.mapTo(mutableSetOf()) { offset -> today.plus(offset, DateTimeUnit.DAY) }

    fun onDateClick(date: LocalDate) {
        val currentState = requireState()
        val newSelectionState = when (val currentSelection = currentState.selectionState) {
            is SelectionState.Empty -> {
                // First click - set start date
                SelectionState.StartSelected(date)
            }

            is SelectionState.StartSelected -> {
                when {
                    date < currentSelection.startDate -> {
                        // Second click on earlier date - reset and start new selection
                        SelectionState.StartSelected(date)
                    }

                    hasDisabledDatesInRange(
                        currentSelection.startDate,
                        date,
                        currentState.disabledDates,
                    ) -> {
                        // Disabled dates in range - reset and start new selection
                        SelectionState.StartSelected(date)
                    }

                    else -> {
                        // Valid range - set end date
                        SelectionState.RangeSelected(DateRange(startDate = currentSelection.startDate, endDate = date))
                    }
                }
            }

            is SelectionState.RangeSelected -> {
                // Third click - reset and start new selection
                SelectionState.StartSelected(date)
            }
        }

        val newDateRange = when (newSelectionState) {
            is SelectionState.Empty -> DateRange()
            is SelectionState.StartSelected -> DateRange(startDate = newSelectionState.startDate)
            is SelectionState.RangeSelected -> newSelectionState.range
        }

        newState { it.copy(selectionState = newSelectionState, selectedRange = newDateRange) }
    }

    private fun hasDisabledDatesInRange(
        start: LocalDate,
        end: LocalDate,
        disabledDates: Set<LocalDate>,
    ): Boolean {
        if (disabledDates.isEmpty()) return false

        var current = start.plus(DAY_INCREMENT, DateTimeUnit.DAY)
        while (current < end) {
            if (current in disabledDates) return true
            current = current.plus(DAY_INCREMENT, DateTimeUnit.DAY)
        }
        return false
    }

    fun clearSelection() {
        newState { it.copy(selectionState = SelectionState.Empty, selectedRange = DateRange()) }
    }

    fun updateDisabledDates(disabledDates: Set<LocalDate>) {
        newState { it.copy(disabledDates = disabledDates) }
    }
}

data class CalendarUiState(
    val today: LocalDate? = null,
    val selectionState: SelectionState = SelectionState.Empty,
    val selectedRange: DateRange = DateRange(),
    val disabledDates: Set<LocalDate> = emptySet(),
    val minDate: LocalDate? = null,
    val maxDate: LocalDate? = null,
)
