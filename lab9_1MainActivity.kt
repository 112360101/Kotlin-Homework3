package com.example.lab9_1

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var progressRabbit = 0
    private var progressTurtle = 0

    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    // 使用父層 Job 統一管理龜兔兩個子協程的生命週期與取消機制
    private var raceJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)

        btnStart.setOnClickListener {
            startRace()
        }
    }

    private fun startRace() {
        // 取消前一場可能殘留的協程並重設狀態
        raceJob?.cancel()
        btnStart.isEnabled = false
        progressRabbit = 0
        progressTurtle = 0
        sbRabbit.progress = 0
        sbTurtle.progress = 0

        raceJob = lifecycleScope.launch {
            // 啟動兔子協程
            launch {
                val sleepProbability = listOf(true, true, false)
                while (progressRabbit < 100 && progressTurtle < 100) {
                    delay(100) // 非阻塞式延遲 0.1 秒
                    if (sleepProbability.random()) {
                        delay(300) // 兔子偷懶睡覺 0.3 秒
                    }
                    progressRabbit = (progressRabbit + 3).coerceAtMost(100)
                    sbRabbit.progress = progressRabbit

                    if (progressRabbit >= 100 && progressTurtle < 100) {
                        finishRace("兔子勝利")
                    }
                }
            }

            // 啟動烏龜協程
            launch {
                while (progressTurtle < 100 && progressRabbit < 100) {
                    delay(100) // 非阻塞式延遲 0.1 秒
                    progressTurtle = (progressTurtle + 1).coerceAtMost(100)
                    sbTurtle.progress = progressTurtle

                    if (progressTurtle >= 100 && progressRabbit < 100) {
                        finishRace("烏龜勝利")
                    }
                }
            }
        }
    }

    private fun finishRace(winnerMsg: String) {
        Toast.makeText(this, winnerMsg, Toast.LENGTH_SHORT).show()
        btnStart.isEnabled = true
        // 任一方抵達終點時，立即取消所有仍在執行的子協程，防止重複彈出提示
        raceJob?.coroutineContext?.cancelChildren()
    }
}
