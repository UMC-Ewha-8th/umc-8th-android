package com.example.flo_clone.ui.main.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentSavedBinding
import com.example.flo_clone.ui.song.SongDatabase

class SavedAlbumFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var adapter: SavedAlbumRVAdapter
    private lateinit var albumDao: AlbumDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        albumDao = SongDatabase.getInstance(requireContext())!!.albumDao()

        adapter = SavedAlbumRVAdapter(mutableListOf()) { album ->
            albumDao.updateIsLikedById(album.id, 0) // false 대신 0 (Int)
            adapter.removeAlbum(album)
            Toast.makeText(requireContext(), "보관함에서 제거되었습니다", Toast.LENGTH_SHORT).show()
        }

        binding.savedRecyclerView.adapter = adapter
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        reloadLikedAlbums()
    }

    private fun reloadLikedAlbums() {
        val likedAlbums = albumDao.getLikedAlbums(1) // true 대신 1 (Int)
        adapter.updateAlbums(likedAlbums)
        Log.d("SavedAlbumFragment", "갱신된 앨범 개수: ${likedAlbums.size}")
    }
}
