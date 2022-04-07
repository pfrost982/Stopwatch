package com.gb.stopwatch.model.time

sealed class StopwatchState {

    data class Paused(val elapsedTime: Long) : StopwatchState()

    data class Running(val startTime: Long, val elapsedTime: Long) : StopwatchState()
}
