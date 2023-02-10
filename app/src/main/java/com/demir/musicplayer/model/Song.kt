package com.demir.musicplayer.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity
@Parcelize
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val songTitle: String?,
    val songArtist: String?,
    val songUri: String?,
    val songDuration: String?,

): Parcelable{

}