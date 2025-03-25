package com.example.week_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// NextActivity 임포트 추가
import com.example.week_1.NextActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 버튼 찾기
        val button1: ImageButton = findViewById(R.id.group_462)
        val button2: ImageButton = findViewById(R.id.group_462_1)
        val button3: ImageButton = findViewById(R.id.group_462_2)
        val button4: ImageButton = findViewById(R.id.group_462__1_)
        val button5: ImageButton = findViewById(R.id.group_462__2_)

        // 버튼 클릭하면 NextActivity로 이동
        val intent = Intent(this, NextActivity::class.java)

        button1.setOnClickListener { startActivity(intent) }
        button2.setOnClickListener { startActivity(intent) }
        button3.setOnClickListener { startActivity(intent) }
        button4.setOnClickListener { startActivity(intent) }
        button5.setOnClickListener { startActivity(intent) }

        // 시스템 바 패딩 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
