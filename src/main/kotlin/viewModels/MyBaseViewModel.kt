package viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Base ViewModel class for desktop Compose applications
 */
open class MyBaseViewModel {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Returns the CoroutineScope tied to this ViewModel
     */
    fun getViewModelScope(): CoroutineScope = viewModelScope

    /**
     * Creates a MutableState with the given initial value
     */
    protected fun <T> mutableStateOf(initialValue: T): MutableState<T> = androidx.compose.runtime.mutableStateOf(initialValue)

    /**
     * Exposes a MutableState as an immutable State
     */
    protected fun <T> MutableState<T>.asState(): State<T> = this

    /**
     * Called when the ViewModel is no longer needed
     */
    fun clear() {
        viewModelScope.cancel()
        onCleared()
    }

    /**
     * Override this to perform any cleanup operations
     */
    protected open fun onCleared() {}
}
