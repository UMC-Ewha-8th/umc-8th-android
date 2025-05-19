package com.example.flo_clone

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentSavedBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SavedAlbumFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding
    private lateinit var albumDB: SongDatabase
    private lateinit var albumRVAdapter: AlbumLockerRVAdapter
    private val selectedAlbums = mutableListOf<Album>()
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        albumDB = SongDatabase.getInstance(requireContext())!!

        val spf = requireContext().getSharedPreferences("auth", MODE_PRIVATE)
        userId = spf.getInt("userIdx", 0)

        initRecyclerView()
        initBottomSheet()
        initSelectAllButton()
        initDeleteButton()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(context)
        albumRVAdapter = AlbumLockerRVAdapter()

        val likedAlbums = albumDB.albumDao().getLikedAlbums(userId) as ArrayList<Album>
        albumRVAdapter.setAlbums(likedAlbums)

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().deleteLikeAlbum(userId, songId)
            }

            override fun onItemClicked(item: Album) {
                toggleSelection(item)
            }

            override fun isItemSelected(item: Album): Boolean {
                return selectedAlbums.contains(item)
            }
        })

        binding.savedRecyclerView.adapter = albumRVAdapter
    }

    private fun toggleSelection(item: Album) {
        if (selectedAlbums.contains(item)) {
            selectedAlbums.remove(item)
        } else {
            selectedAlbums.add(item)
        }

        albumRVAdapter.setSelectedItems(selectedAlbums)

        if (selectedAlbums.isNotEmpty()) {
            showBottomSheet()
        } else {
            hideBottomSheet()
        }
    }

    private fun initSelectAllButton() {
        binding.selectAllTv.setOnClickListener {
            val allSelected = selectedAlbums.size == albumRVAdapter.getAlbums().size
            if (allSelected) {
                selectedAlbums.clear()
                albumRVAdapter.clearSelection()
                hideBottomSheet()
            } else {
                selectedAlbums.clear()
                selectedAlbums.addAll(albumRVAdapter.getAlbums())
                albumRVAdapter.setSelectedItems(selectedAlbums)
                showBottomSheet()
            }
        }
    }

    private fun initBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.bottomSheet.visibility = View.GONE
    }

    private fun showBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheet.visibility = View.VISIBLE
    }

    private fun hideBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.bottomSheet.visibility = View.GONE
    }

    private fun initDeleteButton() {
        binding.sheetDeleteIv.setOnClickListener {
            selectedAlbums.forEach { album ->
                albumDB.albumDao().deleteLikeAlbum(userId, album.id)
                albumRVAdapter.removeAlbum(album)
            }

            selectedAlbums.clear()
            hideBottomSheet()
        }
    }
}
