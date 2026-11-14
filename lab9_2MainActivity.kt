package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var btnCalculate: Button
    private lateinit var edHeight: EditText
    private lateinit var edWeight: EditText
    private lateinit var edAge: EditText
    private lateinit var tvWeightResult: TextView
    private lateinit var tvFatResult: TextView
    private lateinit var tvBmiResult: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar2: ProgressBar
    private lateinit var llProgress: LinearLayout
    private lateinit var btnBoy: RadioButton

    private var calcJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCalculate = findViewById(R.id.btnCalculate)
        edHeight = findViewById(R.id.edHeight)
        edWeight = findViewById(R.id.edWeight)
        edAge = findViewById(R.id.edAge)
        tvWeightResult = findViewById(R.id.tvWeightResult)
        tvFatResult = findViewById(R.id.tvFatResult)
        tvBmiResult = findViewById(R.id.tvBmiResult)
        tvProgress = findViewById(R.id.tvProgress)
        progressBar2 = findViewById(R.id.progressBar2)
        llProgress = findViewById(R.id.llProgress)
        btnBoy = findViewById(R.id.btnBoy)

        btnCalculate.setOnClickListener {
            validateAndCalculate()
        }
    }

    private fun validateAndCalculate() {
        // 使用 toDoubleOrNull() 進行安全轉型，避免輸入小數點等非完整數字時閃退
        val height = edHeight.text.toString().toDoubleOrNull()
        val weight = edWeight.text.toString().toDoubleOrNull()
        val age = edAge.text.toString().toDoubleOrNull()

        when {
            height == null || height <= 0 -> showToast("請輸入正確的身高")
            weight == null || weight <= 0 -> showToast("請輸入正確的體重")
            age == null || age <= 0 -> showToast("請輸入正確的年齡")
            else -> runCoroutines(height, weight, age)
        }
    }

    private fun runCoroutines(height: Double, weight: Double, age: Double) {
        calcJob?.cancel()

        // 初始化顯示狀態並鎖定按鈕避免重複觸發
        tvWeightResult.text = "標準體重\n無"
        tvFatResult.text = "體脂肪\n無"
        tvBmiResult.text = "BMI\n無"
        progressBar2.progress = 0
        tvProgress.text = "0%"
        llProgress.visibility = View.VISIBLE
        btnCalculate.isEnabled = false

        calcJob = lifecycleScope.launch {
            try {
                for (progress in 1..100) {
                    delay(50) // 掛起 50 毫秒，不阻塞主執行緒 UI 操作
                    progressBar2.progress = progress
                    tvProgress.text = "$progress%"
                }

                // 計算 BMI、標準體重與體脂肪
                val bmi = weight / (height / 100).pow(2)
                val (standWeight, bodyFat) = if (btnBoy.isChecked) {
                    Pair((height - 80) * 0.7, 1.39 * bmi + 0.16 * age - 19.34)
                } else {
                    Pair((height - 70) * 0.6, 1.39 * bmi + 0.16 * age - 9)
                }

                tvWeightResult.text = "標準體重 \n${String.format("%.2f", standWeight)}"
                tvFatResult.text = "體脂肪 \n${String.format("%.2f", bodyFat)}"
                tvBmiResult.text = "BMI \n${String.format("%.2f", bmi)}"
            } finally {
                // 無論協程正常結束或中途被取消，皆確保進度條隱藏且按鈕恢復可點擊狀態
                llProgress.visibility = View.GONE
                btnCalculate.isEnabled = true
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
