package com.example.flo_clone.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.flo_clone.ui.signup.SignupActivity
import com.example.flo_clone.data.remote.AuthResponse
import com.example.flo_clone.data.remote.AuthRetrofitInterface
import com.example.flo_clone.databinding.ActivityLoginBinding
import com.example.flo_clone.utils.getRetrofit
import com.example.flo_clone.ui.main.MainActivity
import com.kakao.sdk.user.UserApiClient
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
        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                } else if (token != null) {
                    // ✅ 사용자 정보 요청 후 저장
                    UserApiClient.instance.me { user, _ ->
                        val nickname = user?.kakaoAccount?.profile?.nickname
                        val email = user?.kakaoAccount?.email
                        val profileImg = user?.kakaoAccount?.profile?.profileImageUrl

                        // ✅ SharedPreferences에 저장
                        saveLoginInfo(
                            isKakao = true,
                            nickname = nickname,
                            email = email,
                            profileUrl = profileImg
                        )

                        Toast.makeText(this, "$nickname 님 환영합니다!", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }

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

                        resp.result?.memberId?.let {
                            saveLoginInfo(false, null, null, null) // ✅ 여기 수정
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

    private fun saveLoginInfo(isKakao: Boolean, nickname: String?, email: String?, profileUrl: String?) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putBoolean("isLogin", true)
        editor.putBoolean("isKakaoLogin", isKakao)
        editor.putString("nickname", nickname)
        editor.putString("email", email)
        editor.putString("profileUrl", profileUrl)
        editor.apply()
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KAKAO_USER", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val nickname = user.kakaoAccount?.profile?.nickname
                val email = user.kakaoAccount?.email
                val profileImg = user.kakaoAccount?.profile?.profileImageUrl

                binding.kakaoNicknameTv.text = "닉네임: $nickname"
                binding.kakaoEmailTv.text = "이메일: $email"
                binding.kakaoNicknameTv.visibility = View.VISIBLE
                binding.kakaoEmailTv.visibility = View.VISIBLE

                if (profileImg != null) {



                    Glide.with(this)
                        .load(profileImg)
                        .circleCrop()
                        .into(binding.kakaoProfileIv)
                    binding.kakaoProfileIv.visibility = View.VISIBLE
                }

                Toast.makeText(this, "$nickname 님 환영합니다!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
