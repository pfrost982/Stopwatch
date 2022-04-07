package com.gb.stopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gb.stopwatch.databinding.ActivityMainBinding
import com.gb.stopwatch.databinding.StopwatchItemBinding
import com.gb.stopwatch.viewmodel.StopwatchListOrchestrator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addFab.setOnClickListener {
            createStopwatch()
        }
    }

    private fun createStopwatch() {
        val stopwatchView = StopwatchItemBinding.inflate(layoutInflater)
        val textView = stopwatchView.textTime
        val stopwatch = StopwatchListOrchestrator()
        scope.launch {
            stopwatch.ticker.collect {
                textView.text = it
            }
        }
        stopwatchView.buttonStart.setOnClickListener { stopwatch.start() }
        stopwatchView.buttonPause.setOnClickListener { stopwatch.pause() }
        stopwatchView.buttonStop.setOnClickListener { stopwatch.stop() }
        binding.stopwatch.addView(stopwatchView.root)

    }

    override fun onDestroy() {
        scope.coroutineContext.cancelChildren()
        super.onDestroy()
    }
}