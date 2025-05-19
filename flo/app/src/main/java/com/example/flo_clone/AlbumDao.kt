package com.example.flo_clone

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlbumDao {

    @Insert
    fun insert(album: Album)

    @Insert
    fun likeAlbum(albumLike: Like)

    @Update
    fun updateAlbum(album: Album)

    @Delete
    fun deleteAlbum(album: Album)

    @Query("DELETE FROM AlbumLike WHERE userId = :userId AND albumId = :albumId")
    fun deleteLikeAlbum(userId: Int, albumId: Int)

    @Query("SELECT * FROM AlbumTable")
    fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbumById(id: Int): Album

    @Query("SELECT * FROM AlbumTable WHERE id IN (SELECT albumId FROM AlbumLike WHERE userId = :userId)")
    fun getLikedAlbums(userId: Int): List<Album>

    @Query("SELECT EXISTS (SELECT * FROM AlbumLike WHERE userId = :userId AND albumId = :albumId)")
    fun isLikedAlbum(userId: Int, albumId: Int)


}