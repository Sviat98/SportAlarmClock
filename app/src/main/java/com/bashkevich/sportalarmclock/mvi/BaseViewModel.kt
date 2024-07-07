package com.bashkevich.sportalarmclock.mvi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : UiState, in E : UiEvent, A: UiAction> : ViewModel() {

    abstract val state: Flow<T>

    private val _action: Channel<A> = Channel()
    protected val action = _action.receiveAsFlow()

    protected fun sendAction(action: A){
        viewModelScope.launch {
            _action.send(action)
        }
    }

}

interface UiAction