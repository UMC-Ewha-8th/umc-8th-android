package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }

        binding.loginFinishBtn.setOnClickListener{
            login()
        }
    }

    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailDomainTv.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val email =
            binding.loginIdEt.text.toString() + "@" + binding.loginEmailDomainTv.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email,password)

        user?.let{
            Log.d("LOGIN_ACT_GET_USER","userId : ${user.id}, $user")
            saveJwt(user.id)
            startMainActivity()
        }
        Toast.makeText(this,"회원 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()

    }

    private fun saveJwt(jwt: Int){
        val spf=getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}