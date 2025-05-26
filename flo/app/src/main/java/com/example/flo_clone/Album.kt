package com.example.flo_clone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    var title: String,
    var singer: String,
    var coverImg: Int = R.drawable.default_album_image, // 기본 이미지 ID로 변경
    var isLike: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

