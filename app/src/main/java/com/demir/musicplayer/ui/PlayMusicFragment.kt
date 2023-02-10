package com.demir.musicplayer.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.demir.musicplayer.R
import com.demir.musicplayer.SongViewModel
import com.demir.musicplayer.databinding.FragmentPlayMusicBinding
import com.demir.musicplayer.helper.Constants
import com.demir.musicplayer.model.Song
import com.google.android.material.snackbar.Snackbar


class PlayMusicFragment : Fragment() {
    private lateinit var binding: FragmentPlayMusicBinding
    private val args: PlayMusicFragmentArgs by navArgs()
    private lateinit var song: Song
    private var mMediaPlayer: MediaPlayer? = null
    private var seekLength: Int = 0
    private val seekForwardTime = 5 * 1000
    private val seekBackwardTime = 5 * 1000
    private lateinit var songViewModel:SongViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayMusicBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        song = args.song!!
        mMediaPlayer = MediaPlayer()
        songViewModel=ViewModelProvider(this)[SongViewModel::class.java]
        binding.tvTitle.text = song.songTitle
        binding.tvDuration.text = song.songDuration
        binding.tvAuthor.text = song.songArtist
        binding.ibFavorite.setOnClickListener {
           songViewModel.upsert(song)
            Snackbar.make(view,"Saved Succesfully",Snackbar.LENGTH_SHORT).show()
        }

        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(song.songUri)
        val data = mmr.embeddedPicture

        if (data != null) {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            binding.ibCover.setImageBitmap(bitmap)
        }

        binding.ibPlay.setOnClickListener {
            playSong()
        }

        binding.ibRepeat.setOnClickListener {

            if (!mMediaPlayer!!.isLooping) {
                mMediaPlayer!!.isLooping = true
                binding.ibRepeat.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity?.applicationContext!!,
                        R.drawable.ic_repeat_white
                    )
                )
            } else {
                mMediaPlayer!!.isLooping = false
                binding.ibRepeat.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity?.applicationContext!!,
                        R.drawable.ic_repeat
                    )
                )
            }
        }

        binding.ibForwardSong.setOnClickListener {
            forwardSong()
        }

        binding.ibBackwardSong.setOnClickListener {
            rewindSong()
        }
    }

    private fun forwardSong() {
        if (mMediaPlayer != null) {
            val currentPosition: Int = mMediaPlayer!!.currentPosition
            if (currentPosition + seekForwardTime <= mMediaPlayer!!.duration) {
                mMediaPlayer!!.seekTo(currentPosition + seekForwardTime)
            } else {
                mMediaPlayer!!.seekTo(mMediaPlayer!!.duration)
            }
        }
    }

    private fun rewindSong() {
        if (mMediaPlayer != null) {
            val currentPosition: Int = mMediaPlayer!!.currentPosition
            if (currentPosition - seekBackwardTime >= 0) {
                mMediaPlayer!!.seekTo(currentPosition - seekBackwardTime)
            } else {
                mMediaPlayer!!.seekTo(0)
            }
        }
    }

    private fun playSong() {

        if (!mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.setDataSource(song.songUri)
            mMediaPlayer!!.prepare()
            mMediaPlayer!!.seekTo(seekLength)
            mMediaPlayer!!.start()

            binding.ibPlay.setImageDrawable(
                ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.ic_pause
                )
            )
            updateSeekBar()
        } else {

            mMediaPlayer!!.pause()
            seekLength = mMediaPlayer!!.currentPosition
            binding.ibPlay.setImageDrawable(
                ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.ic_play
                )
            )
        }
    }

    private fun updateSeekBar() {
        if (mMediaPlayer != null) {
            binding.tvCurrentTime.text =
                Constants.durationConverter(mMediaPlayer!!.currentPosition.toLong())
        }
        seekBarSetUp()
        Handler().postDelayed(runnable, 50)
    }

    var runnable = Runnable { updateSeekBar() }

    private fun seekBarSetUp() {

        if (mMediaPlayer != null) {
            binding.seekBar.progress = mMediaPlayer!!.currentPosition
            binding.seekBar.max = mMediaPlayer!!.duration
        }
        binding.seekBar.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView")
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    mMediaPlayer!!.seekTo(progress)
                    binding.tvCurrentTime.text = Constants.durationConverter(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                    if (seekBar != null) {
                        mMediaPlayer!!.seekTo(seekBar.progress)
                    }
                }
            }
        })
    }


    private fun clearMediaPlayer(){
        if (mMediaPlayer != null) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.stop()
            }
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        playSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMediaPlayer()
    }
}