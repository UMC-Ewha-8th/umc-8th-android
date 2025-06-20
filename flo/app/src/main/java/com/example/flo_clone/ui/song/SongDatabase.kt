package com.example.flo_clone.ui.song

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo_clone.ui.signin.UserDao
import com.example.flo_clone.data.entities.Album
import com.example.flo_clone.data.entities.Song
import com.example.flo_clone.data.entities.User
import com.example.flo_clone.ui.main.album.AlbumDao

@Database(entities = [Song::class, User::class, Album::class], version = 4)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song-database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // 개발 단계에서만 허용 (나중에 제거)
                    .build()
            }
            return instance!!
        }
    }
}
