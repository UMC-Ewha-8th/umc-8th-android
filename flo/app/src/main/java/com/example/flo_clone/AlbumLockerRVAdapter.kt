package com.example.flo_clone

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter : RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {

    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
        fun onItemClicked(item: Album)
        fun isItemSelected(item: Album): Boolean
    }

    private lateinit var myItemClickListener: MyItemClickListener

    fun setMyItemClickListener(listener: MyItemClickListener) {
        this.myItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    fun removeSong(position: Int) {
        albums.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemLockerAlbumTitleTv.text = album.title
            binding.itemLockerAlbumSingerTv.text = album.singer
            binding.itemLockerAlbumCoverImgIv.setImageResource(album.coverImg ?: 0)

            binding.itemAlbumMoreIv.setOnClickListener {
                myItemClickListener.onRemoveSong(album.id)
                removeSong(adapterPosition)
            }

            binding.root.setOnClickListener {
                myItemClickListener.onItemClicked(album)
                binding.root.alpha = if (myItemClickListener.isItemSelected(album)) 0.5f else 1f
            }
        }
    }
}
