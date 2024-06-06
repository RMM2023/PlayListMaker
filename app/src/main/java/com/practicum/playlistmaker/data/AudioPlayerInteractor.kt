package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track

interface AudioPlayerInteractor {
    fun setupMediaPlayer(binding : ActivityAudioPlayerBinding, track: Track)
    fun togglePlayPause(binding: ActivityAudioPlayerBinding, isPlaying : Boolean)

}