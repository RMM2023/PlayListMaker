package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track

class AudioPlayerImplementation : AudioPlayerInteractor {
    lateinit var mediaPlayer : MediaPlayer

    override fun setupMediaPlayer(binding : ActivityAudioPlayerBinding, track : Track) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.previewUrl)
            setOnPreparedListener {
                binding.playerTime.text = "00:00"
                binding.buttonPlay.setImageResource(R.drawable.play)
            }
            prepareAsync()
            setOnCompletionListener {
                binding.buttonPlay.setImageResource(R.drawable.play)
                binding.playerTime.text = "00:00"
            }
        }
    }

    override fun togglePlayPause(binding: ActivityAudioPlayerBinding, isPlaying : Boolean) {
        if (isPlaying) {
            mediaPlayer.pause()
            binding.buttonPlay.setImageResource(R.drawable.play)
        } else {
            mediaPlayer.start()
            binding.buttonPlay.setImageResource(R.drawable.pause)
        }
    }

}