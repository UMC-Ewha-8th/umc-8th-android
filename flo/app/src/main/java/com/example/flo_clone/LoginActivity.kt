package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.flo_clone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔘 텍스트 입력될 때마다 버튼 활성화 조건 검사
        binding.loginPasswordEt.addTextChangedListener { enableLoginButtonIfReady() }
        binding.loginIdEt.addTextChangedListener { enableLoginButtonIfReady() }
        binding.loginEmailDomainTv.addTextChangedListener { enableLoginButtonIfReady() }

        // 회원가입 버튼
        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // 로그인 버튼
        binding.loginFinishBtn.setOnClickListener {
            login()
        }
    }

    // 🔐 로그인 로직
    private fun login() {
        val inputEmail = binding.loginIdEt.text.toString() + "@" + binding.loginEmailDomainTv.text.toString()
        val inputPassword = binding.loginPasswordEt.text.toString()

        val userDB = SongDatabase.getInstance(this)  // 또는 UserDatabase.getInstance(this)
        val user = userDB!!.userDao().getUserByEmail(inputEmail)

        if (user != null && user.password == inputPassword) {
            // ✅ 로그인 성공 처리
            val spf = getSharedPreferences("user", MODE_PRIVATE)
            val editor = spf.edit()
            editor.putBoolean("isLogin", true)
            editor.putString("email", inputEmail)
            editor.apply()

            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
            startMainActivity()
        } else {
            Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    // ✅ 모든 입력이 채워졌는지 확인 후 버튼 활성화
    private fun enableLoginButtonIfReady() {
        val email = binding.loginIdEt.text.toString()
        val domain = binding.loginEmailDomainTv.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        binding.loginFinishBtn.isEnabled = email.isNotEmpty() && domain.isNotEmpty() && password.isNotEmpty()
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
