package com.example.flo_clone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.databinding.ItemSavedAlbumBinding

class SavedAlbumRVAdapter(
    private val albums: MutableList<Album>,
    private val onRemoveClick: (Album) -> Unit
) : RecyclerView.Adapter<SavedAlbumRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSavedAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumInfoTv.text = "${album.singer}\n2021.03.25 | 정규 | 댄스 팝"
            album.coverImg?.let { binding.itemAlbumCoverIv.setImageResource(it) }

            binding.itemAlbumMoreBtn.setOnClickListener {
                onRemoveClick(album)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    fun removeAlbum(album: Album) {
        val position = albums.indexOf(album)
        if (position != -1) {
            albums.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // ✅ 반드시 있어야 함
    fun updateAlbums(newAlbums: List<Album>) {
        albums.clear()
        albums.addAll(newAlbums)
        notifyDataSetChanged()
    }
}


