package com.example.flo_clone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumLike(
    val userId: Int,
    val albumId: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
