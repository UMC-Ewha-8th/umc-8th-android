package com.example.flo_clone.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo_clone.ui.main.look.DetailFragment
import com.example.flo_clone.ui.song.SongFragment
import com.example.flo_clone.ui.song.VideoFragment

class AlbumViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment()
            1 -> DetailFragment()
            2 -> VideoFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}