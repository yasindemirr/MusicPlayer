package com.demir.musicplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.demir.musicplayer.model.Song
import com.demir.musicplayer.repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(application:Application):AndroidViewModel(application) {
    val repository=SongRepository(application)

    fun upsert(song: Song)=viewModelScope.launch {
       repository.upsert(song)
    }
    fun deleteSong(song: Song)=viewModelScope.launch {
        repository.deleteSong(song)
    }
    fun readAllSongs(): LiveData<List<Song>> {
        return repository.readAllSongs()
    }

}