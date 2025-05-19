package com.example.flo_clone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Song::class,Album::class,User::class,AlbumLike::class],version=1)

abstract class FloDatabase : RoomDatabase(){
    abstract fun songDao():SongDao
    abstract fun albumDao():AlbumDao
    abstract fun userDao():UserDao

    companion object{
        private var instance: FloDatabase?=null

        @Synchronized
        fun getInstance(context:Context): FloDatabase?{
            if(instance==null){
                synchronized(FloDatabase::class ){
                    instance= Room.databaseBuilder(
                            context.applicationContext,
                            FloDatabase::class.java,
                            "song-database"
                            ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}