package com.example.flo_clone.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.example.flo_clone.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding

    private val tabs = listOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        if (arguments != null && arguments?.getString("title") != null && arguments?.getString("singer") != null) {
            val title = arguments!!.getString("title")
            val singer = arguments!!.getString("singer")
            binding.albumMusicTitleTv.text = title
            binding.albumSingerNameTv.text = singer
        }

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity)
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumContentVp.adapter = AlbumViewPagerAdapter(this)
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
    }
}