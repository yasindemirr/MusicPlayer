package com.demir.musicplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demir.musicplayer.model.Song


@Database(entities = [Song::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun getSongDao():SongDao
    companion object{
        @Volatile private var instance: SongDatabase?=null
        private val lock=Any()
        operator fun invoke(context: Context)= instance?: synchronized(lock){
            instance?: makeDataBase(context).also {
                instance=it
            }
        }
        private fun makeDataBase(context: Context)= Room.databaseBuilder(
            context.applicationContext,SongDatabase::class.java,"fooddatabase").build()


    }
}
