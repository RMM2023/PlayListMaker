package com.practicum.playlistmaker.data

import android.os.Handler
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding

interface AudioPlayerInteractor {
    fun setupMediaPlayer(binding : ActivityAudioPlayerBinding, track: Track)
    fun togglePlayPause(binding: ActivityAudioPlayerBinding, isPlaying : Boolean)

}