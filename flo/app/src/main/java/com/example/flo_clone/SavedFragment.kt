package com.example.flo_clone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {

    lateinit var binding: FragmentSavedBinding
    lateinit var songDB: SongDatabase
    private lateinit var savedAdapter: SavedSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        setupRecyclerView()
        setupPlayAllButton()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadLikedSongs()
    }

    private fun setupRecyclerView() {
        savedAdapter = SavedSongRVAdapter()
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.savedRecyclerView.adapter = savedAdapter

        savedAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
                loadLikedSongs()
            }
        })
    }

    private fun setupPlayAllButton() {
        // 전체듣기 텍스트뷰 또는 아이콘 클릭 시 반응
        val playAllClickListener = View.OnClickListener {
            val likedSongs = songDB.songDao().getLikedSongs(true)
            if (likedSongs.isNotEmpty()) {
                playAllSongs(likedSongs)
            }
        }

        binding.playAllTv.setOnClickListener(playAllClickListener)
        binding.playAllIv.setOnClickListener(playAllClickListener)
    }

    private fun playAllSongs(songs: List<Song>) {
        val firstSong = songs[0]
        // 예시: MiniPlayer에 첫 곡 세팅 (MainActivity에 메서드 있어야 함)
        (activity as? MainActivity)?.setMiniPlayer(firstSong)

        // 또는 로그로만 확인해도 됨 (디버깅용)
        Log.d("SavedFragment", "전체 재생 시작: ${songs.map { it.title }}")
    }


    private fun loadLikedSongs() {
        val likedSongs = songDB.songDao().getLikedSongs(true) as ArrayList<Song>
        savedAdapter.addSongs(likedSongs)
    }
}