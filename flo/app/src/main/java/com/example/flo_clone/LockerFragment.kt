package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentLockerBinding
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
                else -> ""
            }
        }.attach()

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        updateLoginText()
    }

    private fun updateLoginText() {
        val spf = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val isLogin = spf.getBoolean("isLogin", false)

        binding.loginTv.text = if (isLogin) "로그아웃" else "로그인"

        binding.loginTv.setOnClickListener {
            if (isLogin) {
                // 로그아웃 처리
                val editor = spf.edit()
                editor.remove("isLogin")
                editor.apply()

                Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                updateLoginText() // 다시 로그인 상태로 표시 변경
            } else {
                // 로그인 화면으로 이동
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
