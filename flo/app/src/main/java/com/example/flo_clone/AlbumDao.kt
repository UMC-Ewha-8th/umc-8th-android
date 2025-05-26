package com.example.flo_clone

import androidx.room.*

@Dao
interface AlbumDao {

    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM AlbumTable WHERE isLike = :isLike")
    fun getLikedAlbums(isLike: Int): List<Album>

    @Query("UPDATE AlbumTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikedById(id: Int, isLike: Int)

    @Query("SELECT * FROM AlbumTable WHERE title = :title AND singer = :singer")
    fun getAlbumByTitleAndSinger(title: String, singer: String): Album?
}

