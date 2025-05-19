package com.example.flo_clone

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userRepository: UserRepository
    private val emailDomains = arrayOf("naver.com", "gmail.com", "hanmail.net", "nate.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        userRepository = UserRepository(this)

        setContentView(binding.root)

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emailDomains)
        binding.signupEmailDomainTv.setAdapter(adapter)

        binding.signupFinishBtn.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                signup()
                finish()
            }
        }
    }
    private fun signup() {
        val email =
            binding.signupIdEt.text.toString() + "@" + binding.signupEmailDomainTv.text.toString()
        val password = binding.signupPasswordEt.text.toString()

        val user = User(email, password)
        userRepository.insert(user)
    }


    private fun validateEmail(): Boolean {
        val email = binding.signupIdEt.text.toString()
        val emailDomain = binding.signupEmailDomainTv.text.toString()

        if (email.isEmpty()) {
            binding.signupValidateTv.text = resources.getString(R.string.signup_validate_id_empty)
            binding.signupValidateTv.visibility = View.VISIBLE
            Toast.makeText(this,"이메일 형식이 잘못되었습니다",Toast.LENGTH_SHORT).show()
            return false
        }

        if (emailDomain.isEmpty()) {
            binding.signupValidateTv.text =
                resources.getString(R.string.signup_validate_email_empty)
            binding.signupValidateTv.visibility = View.VISIBLE
            return false
        }

        if (!emailDomain.contains('.')) {
            binding.signupValidateTv.text = resources.getString(R.string.signup_validate_email)
            binding.signupValidateTv.visibility = View.VISIBLE
            return false
        }

        val emailFull = "$email@$emailDomain"
        val user = userRepository.getUserByEmail(emailFull)

        if (user != null) {
            binding.signupValidateTv.text =
                resources.getString(R.string.signup_validate_email_exist)
            binding.signupValidateTv.visibility = View.VISIBLE
            return false
        }

        binding.signupValidateTv.visibility = View.GONE

        return true
    }

    private fun validatePassword(): Boolean {
        val password = binding.signupPasswordEt.text.toString()
        val passwordCheck = binding.signupPasswordCheckEt.text.toString()

        if (password.isEmpty()) {
            binding.signupValidateTv.text = resources.getString(R.string.signup_validate_pw_empty)
            binding.signupValidateTv.visibility = View.VISIBLE
            Toast.makeText(this,"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
            return false
        }

        if (passwordCheck.isEmpty()) {
            binding.signupValidateTv.text =
                resources.getString(R.string.signup_validate_pw_check_empty)
            binding.signupValidateTv.visibility = View.VISIBLE
            return false
        }

        if (password != passwordCheck) {
            binding.signupValidateTv.text = resources.getString(R.string.signup_validate_pw)
            binding.signupValidateTv.visibility = View.VISIBLE
            return false
        }

        binding.signupValidateTv.visibility = View.GONE

        return true
    }


}