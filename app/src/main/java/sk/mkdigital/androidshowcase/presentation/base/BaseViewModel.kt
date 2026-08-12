package sk.mkdigital.androidshowcase.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import sk.mkdigital.androidshowcase.domain.exceptions.base.BaseException
import sk.mkdigital.androidshowcase.domain.exceptions.base.UnknownException
import sk.mkdigital.androidshowcase.util.Logger
import javax.inject.Inject
import sk.mkdigital.androidshowcase.util.suspendRunCatching

abstract class BaseViewModel<STATE : Any>(
    defaultState: STATE,
    private val logsScreenName: Boolean = true,
) : ViewModel() {

    @Inject
    lateinit var logger: Logger

    protected val tag = this::class.simpleName

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(defaultState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    private val _navEvent = MutableSharedFlow<NavEvent>()
    val navEvent: SharedFlow<NavEvent> = _navEvent.asSharedFlow()

    private val scope get() = viewModelScope

    @Suppress("unused") // Called by Hilt after field injection
    @Inject
    fun afterInit() {
        logScreenName()
        loadInitialData()
    }

    private fun logScreenName() {
        if (!logsScreenName) return
        val screenName = tag?.removeSuffix("ViewModel") ?: return
        logger.d("Screen: $screenName")
    }

    protected open fun loadInitialData() {}

    open fun onResume() {
    }

    open fun onPause() {
    }

    protected fun navigate(event: NavEvent) {
        viewModelScope.launch { _navEvent.emit(event) }
    }

    protected fun newState(stateCopy: (STATE) -> STATE) {
        _state.value = stateCopy(_state.value)
    }

    protected fun requireState(block: (STATE) -> Unit): Unit = block(_state.value)

    protected fun requireState(): STATE = _state.value

    protected fun <T> execute(
        action: suspend () -> T,
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onError: (BaseException) -> Unit = {}
    ): Job = scope.launch {
        onLoading()
        suspendRunCatching { action() }
            .onSuccess(onSuccess)
            .onFailure { report(it, onError) }
    }

    private fun report(error: Throwable, onError: (BaseException) -> Unit) {
        logger.e("${tag}: ${error.message}", error)
        onError(error as? BaseException ?: UnknownException(error))
    }

    protected fun <T> observe(
        onStart: (suspend () -> Unit)? = null,
        flow: Flow<T>,
        onEach: (T) -> Unit,
        onError: (BaseException) -> Unit = {}
    ): Job = scope.launch {
        suspendRunCatching { onStart?.invoke() }
            .onFailure {
                report(it, onError)
                return@launch
            }
        flow.catch { e ->
            when (e) {
                is BaseException -> {
                    logger.e("${tag}: ${e.message}", e)
                    onError(e)
                }

                else -> {
                    logger.e("${tag}: ${e.message}", e)
                    onError(UnknownException(e))
                }
            }
        }.collect { onEach(it) }
    }
}

interface NavEvent
