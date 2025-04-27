package com.dosei.fit.workout.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dosei.fit.workout.data.model.UiError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    protected val _error = Channel<UiError>()
    val error = _error.receiveAsFlow()

    fun <T> request(
        block: suspend () -> T
    ) {
        viewModelScope.launch {
            _isLoading.emit(true)
            runCatching { block() }
                .onSuccess { _isLoading.emit(false) }
                .onFailure {
                    _isLoading.emit(false)
                    _error.send(UiError(message = it.message.orEmpty(), cause = it))
                }
        }
    }

}