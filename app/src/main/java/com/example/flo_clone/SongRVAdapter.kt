import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.R
import com.example.flo_clone.Song
import com.example.flo_clone.databinding.ItemSongBinding

class SongRVAdapter(private val songList: ArrayList<Song>) : RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    interface MyItemClickListener {
        fun onPlaySong(song: Song)
        fun onRemoveSong(position: Int)
    }

    inner class ViewHolder(val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
            binding.itemSongCoverImgIv.setImageResource(song.coverImg ?: R.drawable.img_album_exp)

            binding.itemSongPlayBtn.setOnClickListener {
                song.isPlaying = !song.isPlaying
                mItemClickListener.onPlaySong(song)
                notifyItemChanged(adapterPosition)
            }

            binding.itemSongMoreBtn.setOnClickListener {
                mItemClickListener.onRemoveSong(adapterPosition)

            }

            if (song.isPlaying) {
                binding.itemSongPlayBtn.setImageResource(R.drawable.btn_miniplay_pause)
            } else {
                binding.itemSongPlayBtn.setImageResource(R.drawable.btn_miniplayer_play)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songList.size

    // 🔥 이 부분 추가
    fun removeItem(position: Int) {
        songList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, songList.size)
    }

}

