package com.example.flo_clone

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.ui.search.SearchFragment
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
    private var gson: Gson = Gson()
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var updateSeekBarRunnable: Runnable
    private var mediaPlayer: MediaPlayer? = null


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                Toast.makeText(this, data?.getStringExtra("title"), Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        // SongActivity로 이동
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                putExtra("title", song.title)
                putExtra("singer", song.singer)
                putExtra("second", song.second)
                putExtra("playTime", song.playTime)
                putExtra("isPlaying", song.isPlaying)
                putExtra("music", song.music)
            }
            startForResult.launch(intent)
        }

        initSeekBarUpdater()
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, HomeFragment())
            .commitAllowingStateLoss()

        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, HomeFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.navigation_look -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, LookFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, SearchFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.navigation_locker -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, LockerFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
    }

    fun setMiniPlayer(song: Song) {
        this.song = song
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.max = song.playTime
        binding.mainMiniplayerProgressSb.progress = song.second

        updateMiniPlayerButton(song.isPlaying)

        // 기존 mediaPlayer 정리
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resources.getIdentifier(song.music, "raw", packageName))
        mediaPlayer?.seekTo(song.second * 1000)

        if (song.isPlaying) {
            mediaPlayer?.start()
        }

        binding.mainMiniplayerPlayIv.setOnClickListener {
            song.isPlaying = !song.isPlaying
            updateMiniPlayerButton(song.isPlaying)

            if (song.isPlaying) {
                mediaPlayer?.start()
            } else {
                mediaPlayer?.pause()
            }
        }

        startSeekBarUpdater()
    }


    private fun updateMiniPlayerButton(isPlaying: Boolean) {
        if (isPlaying) {
            binding.mainMiniplayerPlayIv.setImageResource(R.drawable.btn_miniplay_pause)
        } else {
            binding.mainMiniplayerPlayIv.setImageResource(R.drawable.btn_miniplayer_play)
        }
    }

    private fun initSeekBarUpdater() {
        updateSeekBarRunnable = object : Runnable {
            override fun run() {
                if (song.isPlaying) {
                    song.second++
                    if (song.second >= song.playTime) {
                        song.second = 0
                        song.isPlaying = false
                        updateMiniPlayerButton(false)
                    }
                    binding.mainMiniplayerProgressSb.progress = song.second
                }
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun startSeekBarUpdater() {
        handler.removeCallbacks(updateSeekBarRunnable)
        handler.post(updateSeekBarRunnable)
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if (songJson == null) {
            Song("라일락", "아이유(IU)", 0, 60, false, "iu_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }
        setMiniPlayer(song)
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE).edit()
        val songJson = gson.toJson(song)
        sharedPreferences.putString("songData", songJson)
        sharedPreferences.apply()

        handler.removeCallbacks(updateSeekBarRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
    }

}
