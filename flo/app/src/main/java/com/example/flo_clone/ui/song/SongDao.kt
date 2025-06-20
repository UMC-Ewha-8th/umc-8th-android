package com.example.flo_clone.ui.song

import androidx.room.*
import com.example.flo_clone.data.entities.Song

@Dao
interface SongDao {

    @Insert
    fun insert(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(id: Int, isLike: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Int): List<Song>
}

