package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {
    lateinit var binding: FragmentSavedBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadLikedSongs()
    }

    private fun loadLikedSongs() {
        val songs = songDB.songDao().getLikedSongs(1) as ArrayList<Song> // 1 = true
        val adapter = SavedSongRVAdapter()
        adapter.addSongs(songs)

        adapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(songId, 0) // 0 = false
                loadLikedSongs()
            }
        })

        binding.savedRecyclerView.adapter = adapter
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


}
