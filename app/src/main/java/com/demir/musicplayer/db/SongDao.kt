package com.demir.musicplayer.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.demir.musicplayer.model.Song

@Dao
interface SongDao {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun upsert(song: Song)
    @Query("SELECT*FROM song ORDER BY id DESC")
    fun readAllSong(): LiveData<List<Song>>
    @Delete
    suspend fun deleteSong(song:Song)
}