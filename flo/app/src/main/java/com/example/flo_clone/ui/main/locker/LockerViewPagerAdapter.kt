package com.example.flo_clone.ui.main.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo_clone.ui.song.MusicFileFragment
import com.example.flo_clone.ui.main.album.SavedAlbumFragment

class LockerViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedFragment()
            1 -> MusicFileFragment()
            2 -> SavedAlbumFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}