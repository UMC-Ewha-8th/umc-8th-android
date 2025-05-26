package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.flo_clone.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginPasswordEt.addTextChangedListener { enableLoginButtonIfReady() }
        binding.loginIdEt.addTextChangedListener { enableLoginButtonIfReady() }
        binding.loginEmailDomainTv.addTextChangedListener { enableLoginButtonIfReady() }

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.loginFinishBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.loginIdEt.text.toString() + "@" + binding.loginEmailDomainTv.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        val request = LoginRequest(email = email, password = password)

        Log.d("LOGIN_REQUEST", "email=$email, password=$password")

        authService.login(request).enqueue(object : retrofit2.Callback<AuthResponse> {
            override fun onResponse(call: retrofit2.Call<AuthResponse>, response: retrofit2.Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()

                        resp.result?.memberId?.let { token ->
                            saveJwt(token)
                        }

                        startMainActivity()
                    } else if (resp != null) {
                        val msg = when (resp.code) {
                            "AUTH_014" -> "존재하지 않는 계정입니다. 회원가입을 진행해주세요."
                            "AUTH_008" -> "비밀번호를 잘못 입력했습니다."
                            "AUTH_009", "AUTH_010" -> "인증에 실패했습니다."
                            else -> "로그인에 실패했습니다."
                        }
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Log.e("API_ERROR", "서버 응답 실패: ${response.code()}, $errorMsg")
                    val parsedMsg = parseErrorMessage(errorMsg)
                    Toast.makeText(this@LoginActivity, parsedMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val json = JSONObject(errorBody ?: "")
            when (json.getString("code")) {
                "AUTH_014" -> "존재하지 않는 계정입니다. 회원가입을 진행해주세요."
                "AUTH_008" -> "비밀번호를 잘못 입력했습니다."
                "AUTH_009", "AUTH_010" -> "인증에 실패했습니다."
                else -> json.getString("message")
            }
        } catch (e: Exception) {
            "서버 오류 발생"
        }
    }

    private fun enableLoginButtonIfReady() {
        val email = binding.loginIdEt.text.toString()
        val domain = binding.loginEmailDomainTv.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        binding.loginFinishBtn.isEnabled = email.isNotEmpty() && domain.isNotEmpty() && password.isNotEmpty()
    }

    private fun saveJwt(memberId: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putBoolean("isLogin", true)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
