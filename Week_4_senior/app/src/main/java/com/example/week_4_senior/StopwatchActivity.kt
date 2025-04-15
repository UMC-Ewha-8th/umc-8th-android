package com.example.week_4_senior

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week_4_senior.databinding.ActivityStopwatchBinding

class StopwatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStopwatchBinding
    private var isRunning = false
    private var milliseconds = 0
    private var stopwatchThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startPauseBtn.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                binding.startPauseBtn.text = "Pause"
                startStopwatch() // 이때만 Thread 시작
            } else {
                isRunning = false
                binding.startPauseBtn.text = "Start"
            }
        }

        binding.clearBtn.setOnClickListener {
            milliseconds = 0
            binding.timeText.text = formatTime(milliseconds)
        }
    }

    private fun startStopwatch() {
        if (stopwatchThread?.isAlive == true) return

        stopwatchThread = Thread {
            while (true) {
                if (isRunning) {
                    Thread.sleep(10)
                    milliseconds++

                    runOnUiThread {
                        binding.timeText.text = formatTime(milliseconds)
                    }
                }
            }
        }
        stopwatchThread?.start()
    }

    private fun formatTime(ms: Int): String {
        val totalSeconds = ms / 100
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val hundredths = ms % 100
        return String.format("%02d:%02d.%02d", minutes, seconds, hundredths)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopwatchThread?.interrupt()
    }
}

