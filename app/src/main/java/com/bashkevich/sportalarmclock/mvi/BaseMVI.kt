package com.bashkevich.sportalarmclock.mvi

import kotlinx.coroutines.flow.*

abstract class Reducer<S : UiState, E : UiEvent>(initialVal: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialVal)
    val state: StateFlow<S>
        get() = _state



    fun sendEvent(event: E) {
        reduce(_state.value, event)
    }

    fun setState(newState: S) {
        val success = _state.tryEmit(newState)

    }

    abstract fun reduce(oldState: S, event: E)
}

interface UiState

interface UiEvent
