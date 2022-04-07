package com.gb.stopwatch.viewmodel

import com.gb.stopwatch.model.time.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchListOrchestrator(private val scope: CoroutineScope) {

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    fun createStopwatch(): Stopwatch {
        return Stopwatch(scope, timestampProvider)
    }
}

class Stopwatch(private val scope: CoroutineScope, timestampProvider: TimestampProvider) {
    private val stopwatchStateHolder = StopwatchStateHolder(
        StopwatchStateCalculator(
            timestampProvider,
            ElapsedTimeCalculator(timestampProvider)
        ),
        ElapsedTimeCalculator(timestampProvider),
        TimestampMillisecondsFormatter()
    )

    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(TimestampMillisecondsFormatter.DEFAULT_TIME)
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        if (job == null) startJob()
        stopwatchStateHolder.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                mutableTicker.value = stopwatchStateHolder.getStringTimeRepresentation()
                delay(20)
            }
        }
    }

    fun pause() {
        stopwatchStateHolder.pause()
        stopJob()
    }

    fun stop() {
        stopwatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun clearValue() {
        mutableTicker.value = TimestampMillisecondsFormatter.DEFAULT_TIME
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }
}
