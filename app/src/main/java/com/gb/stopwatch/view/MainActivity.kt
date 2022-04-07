package com.gb.stopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gb.stopwatch.databinding.ActivityMainBinding
import com.gb.stopwatch.databinding.StopwatchItemBinding
import com.gb.stopwatch.viewmodel.StopwatchListOrchestrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val stopwatchListOrchestrator =
        StopwatchListOrchestrator(CoroutineScope(Dispatchers.Main + SupervisorJob()))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createStopwatch()
    }

    private fun createStopwatch(){
        val stopwatchView = StopwatchItemBinding.inflate(layoutInflater)
        val textView = stopwatchView.textTime
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchListOrchestrator.ticker.collect {
                textView.text = it
            }
        }
        stopwatchView.buttonStart.setOnClickListener { stopwatchListOrchestrator.start() }
        stopwatchView.buttonPause.setOnClickListener { stopwatchListOrchestrator.pause() }
        stopwatchView.buttonStop.setOnClickListener { stopwatchListOrchestrator.stop() }
        binding.scrollView.addView(stopwatchView.root)

    }
}