package com.demir.musicplayer.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.demir.musicplayer.db.SongDatabase
import com.demir.musicplayer.model.Song

class SongRepository(application:Application) {
    val db=SongDatabase(application)
    suspend fun upsert(song: Song){
        db.getSongDao().upsert(song)
    }
    suspend fun deleteSong(song: Song){
        db.getSongDao().deleteSong(song)
    }
    fun readAllSongs():LiveData<List<Song>>{
        return db.getSongDao().readAllSong()
    }
}