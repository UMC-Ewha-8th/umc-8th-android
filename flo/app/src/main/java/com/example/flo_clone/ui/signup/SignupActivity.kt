package com.example.flo_clone.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.ui.song.SongDatabase
import com.example.flo_clone.data.entities.User
import com.example.flo_clone.data.remote.AuthResponse
import com.example.flo_clone.data.remote.AuthRetrofitInterface
import com.example.flo_clone.databinding.ActivitySignupBinding
import com.example.flo_clone.ui.signin.LoginActivity
import com.example.flo_clone.utils.getRetrofit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val emailDomains = arrayOf("naver.com", "gmail.com", "hanmail.net", "nate.com")
    private lateinit var userDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDB = SongDatabase.getInstance(this) ?: run {
            Toast.makeText(this, "DB 초기화 실패", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emailDomains)
        binding.signupEmailDomainTv.setAdapter(adapter)

        binding.signupFinishBtn.setOnClickListener {
            if (validateEmail() && validatePassword() && validateName()) {
                signup()
            }
        }

        Thread {
            try {
                val users = userDB.userDao().getAllUsers()
                for (user in users) {
                    Log.d("UserCheck", "ID: ${user.id}, Email: ${user.email}, Password: ${user.password}, Name: ${user.name}")
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "DB 로드 실패: ${e.message}")
            }
        }.start()
    }

    private fun signup() {
        val email = binding.signupIdEt.text.toString() + "@" + binding.signupEmailDomainTv.text.toString()
        val password = binding.signupPasswordEt.text.toString()
        val name = binding.signupNameEt.text.toString()

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        val request = SignupRequest(email = email, password = password, name = name)

        Log.d("SIGNUP_REQUEST", "email=$email, password=$password, name=$name")

        authService.signUp(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        Toast.makeText(this@SignupActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()

                        Thread {
                            try {
                                userDB.userDao().insert(User(email, password, name))
                            } catch (e: Exception) {
                                Log.e("DB_ERROR", "DB 저장 실패: ${e.message}")
                            }
                        }.start()

                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()

                    } else if (resp != null) {
                        val msg = when (resp.code) {
                            "AUTH_015" -> "이미 존재하는 유저입니다."
                            else -> "회원가입에 실패했습니다."
                        }
                        binding.signupValidateTv.text = msg
                        binding.signupValidateTv.visibility = View.VISIBLE
                        Toast.makeText(this@SignupActivity, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignupActivity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Log.e("API_ERROR", "서버 응답 실패: ${response.code()}, $errorMsg")
                    val parsedMsg = parseErrorMessage(errorMsg)
                    binding.signupValidateTv.text = parsedMsg
                    binding.signupValidateTv.visibility = View.VISIBLE
                    Toast.makeText(this@SignupActivity, parsedMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val json = JSONObject(errorBody ?: "")
            when (json.getString("code")) {
                "AUTH_015" -> "이미 존재하는 유저입니다."
                else -> json.getString("message")
            }
        } catch (e: Exception) {
            "서버 오류 발생"
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.signupIdEt.text.toString()
        val emailDomain = binding.signupEmailDomainTv.text.toString()
        return if (email.isEmpty() || emailDomain.isEmpty() || !emailDomain.contains(".")) {
            binding.signupValidateTv.text = "올바른 이메일을 입력하세요"
            binding.signupValidateTv.visibility = View.VISIBLE
            false
        } else {
            binding.signupValidateTv.visibility = View.GONE
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.signupPasswordEt.text.toString()
        val passwordCheck = binding.signupPasswordCheckEt.text.toString()
        return if (password.isEmpty() || password != passwordCheck) {
            binding.signupValidateTv.text = "비밀번호가 일치하지 않습니다"
            binding.signupValidateTv.visibility = View.VISIBLE
            false
        } else {
            binding.signupValidateTv.visibility = View.GONE
            true
        }
    }

    private fun validateName(): Boolean {
        val name = binding.signupNameEt.text.toString()
        return if (name.isEmpty()) {
            binding.signupValidateTv.text = "이름을 입력하세요"
            binding.signupValidateTv.visibility = View.VISIBLE
            false
        } else {
            binding.signupValidateTv.visibility = View.GONE
            true
        }
    }
}
