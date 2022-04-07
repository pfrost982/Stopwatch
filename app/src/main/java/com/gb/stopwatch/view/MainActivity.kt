package com.gb.stopwatch.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gb.stopwatch.R
import com.gb.stopwatch.model.time.*
import com.gb.stopwatch.viewmodel.StopwatchListOrchestrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text_time)
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchListOrchestrator.ticker.collect {
                textView.text = it
            }
        }

        findViewById<Button>(R.id.button_start).setOnClickListener {
            stopwatchListOrchestrator.start()
        }

        findViewById<Button>(R.id.button_pause).setOnClickListener {
            stopwatchListOrchestrator.pause()
        }

        findViewById<Button>(R.id.button_stop).setOnClickListener {
            stopwatchListOrchestrator.stop()
        }
    }
}