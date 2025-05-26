package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()
    private val tabs = listOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumViewPagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title
        binding.albumSingerNameTv.text = album.singer

        val albumDao = SongDatabase.getInstance(requireContext())!!.albumDao()

        // DB에 없으면 insert 후 가져오기
        var dbAlbum = albumDao.getAlbumByTitleAndSinger(album.title, album.singer)

        if (dbAlbum == null) {
            albumDao.insert(album)
            dbAlbum = albumDao.getAlbumByTitleAndSinger(album.title, album.singer)
        }

        // 초기 하트 상태 반영
        binding.albumLikeIv.setImageResource(
            if (dbAlbum?.isLike == true) R.drawable.ic_my_like_on
            else R.drawable.ic_my_like_off
        )

        // 하트 버튼 클릭 리스너
        binding.albumLikeIv.setOnClickListener {
            val existingAlbum = albumDao.getAlbumByTitleAndSinger(album.title, album.singer)

            if (existingAlbum != null) {
                val newIsLike = if (existingAlbum.isLike) 0 else 1
                albumDao.updateIsLikedById(existingAlbum.id, newIsLike)
                album.isLike = (newIsLike == 1)
            }

            // 아이콘 변경
            binding.albumLikeIv.setImageResource(
                if (album.isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
            )

            // 토스트
            Toast.makeText(
                requireContext(),
                if (album.isLike) "앨범이 보관함에 추가되었습니다" else "앨범이 보관함에서 제거되었습니다",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
