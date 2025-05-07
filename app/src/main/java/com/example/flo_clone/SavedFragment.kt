package com.example.flo_clone

import SongRVAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)

        val songList = arrayListOf(
            Song("LILAC", "아이유", R.drawable.img_album_exp2),
            Song("Butter", "BTS", R.drawable.img_album_exp),
            Song("Next Level", "에스파", R.drawable.img_album_exp3),
            Song("Weekend", "태연", R.drawable.img_album_exp6)
        )

        val adapter = SongRVAdapter(songList)
        binding.savedRecyclerView.adapter = adapter
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener {
            override fun onPlaySong(song: Song) {
                (activity as MainActivity).setMiniPlayer(song)
            }

            override fun onRemoveSong(position: Int) {
                adapter.removeItem(position)
            }
        })

        return binding.root
    }

}
