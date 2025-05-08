package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private var gson:Gson= Gson()

    private val tabs = listOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson=arguments?.getString("album")
        val album=gson.fromJson(albumJson,Album::class.java)
        setInit(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity)
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter=AlbumViewPagerAdapter(this)
        binding.albumContentVp.adapter = AlbumViewPagerAdapter(this)
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album:Album){
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text=album.title.toString()
        binding.albumSingerNameTv.text=album.singer.toString()
    }
}