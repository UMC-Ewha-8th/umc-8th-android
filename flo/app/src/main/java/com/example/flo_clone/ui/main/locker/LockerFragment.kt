package com.example.flo_clone.ui.main.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo_clone.databinding.FragmentLockerBinding
import com.example.flo_clone.ui.signin.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    lateinit var binding: FragmentLockerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        // ViewPager2 Adapter 연결
        val pagerAdapter = LockerViewPagerAdapter(this)
        binding.lockerContentVp.adapter = pagerAdapter

        // TabLayout 연결
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = when (position) {
                0 -> "저장한 곡"
                1 -> "음악파일"
                2 -> "저장앨범"
                else -> ""
            }
        }.attach()

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        updateLoginText()
        updateUserInfo()
    }

    private fun updateLoginText() {
        val spf = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val isLogin = spf.getBoolean("isLogin", false)
        val isKakaoLogin = spf.getBoolean("isKakaoLogin", false)

        binding.loginTv.text = if (isLogin) "로그아웃" else "로그인"

        binding.loginTv.setOnClickListener {
            if (isLogin) {
                if (isKakaoLogin) {
                    com.kakao.sdk.user.UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Toast.makeText(requireContext(), "카카오 로그아웃 실패", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                            clearLoginInfo()
                            updateLoginText()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                    clearLoginInfo()
                    updateLoginText()
                }
            } else {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUserInfo() {
        val spf = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val nickname = spf.getString("nickname", "")
        val profileUrl = spf.getString("profileUrl", "")

        if (!nickname.isNullOrEmpty()) {
            binding.nicknameTextView.visibility = View.VISIBLE
            binding.nicknameTextView.text = "닉네임: $nickname"
        }

        if (!profileUrl.isNullOrEmpty()) {
            binding.profileImageView.visibility = View.VISIBLE
            Glide.with(this)
                .load(profileUrl)
                .circleCrop()
                .into(binding.profileImageView)
        }
    }

    private fun clearLoginInfo() {
        val spf = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        spf.edit().clear().apply()
    }



}
