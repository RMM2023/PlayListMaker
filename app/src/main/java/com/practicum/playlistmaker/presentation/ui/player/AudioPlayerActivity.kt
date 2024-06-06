package com.practicum.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.data.utils.GlideImageLoaderImpl
import com.practicum.playlistmaker.data.utils.MediaPlayerManagerImpl
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.presentation.model.TrackUIModel
import com.practicum.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.practicum.playlistmaker.domain.usecase.GetTrackUseCase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var isPlaying = false

    private lateinit var binding: ActivityAudioPlayerBinding

    private val debounceInterval = 1000L
    private var lastClickTime = 0L
    private val debounceHandler = Handler(Looper.getMainLooper())
    private val debounceRunnable = Runnable {
        lastClickTime = 0L
    }

    private val viewModel: AudioPlayerViewModel by lazy {
        AudioPlayerViewModel(
            GetTrackUseCase(TrackRepositoryImpl()),
            MediaPlayerManagerImpl(),
            GlideImageLoaderImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra(CURRENT_TRACK)
        handler = Handler(Looper.getMainLooper())

        viewModel.getTrack(trackJson, object : AudioPlayerViewModel.Callback<TrackUIModel> {
            override fun onSuccess(data: TrackUIModel) {
                bindTrackData(data)

                viewModel.prepareMediaPlayer(
                    data.previewUrl,
                    onPrepared = {
                        binding.playerTime.text = "00:00"
                        binding.buttonPlay.setImageResource(R.drawable.play)
                    },
                    onCompletion = {
                        binding.buttonPlay.setImageResource(R.drawable.play)
                        binding.playerTime.text = "00:00"
                        handler.removeCallbacks(updateTimeTask)
                    }
                )
            }

            override fun onError(error: Throwable) {
                Toast.makeText(this@AudioPlayerActivity, "Error loading track data", Toast.LENGTH_SHORT).show()
                finish()
            }
        })





        binding.buttonBackPlayer.setOnClickListener { finish() }

        binding.buttonPlay.setOnClickListener { handleDebouncedClick { togglePlayPause() } }

        binding.buttonLike.setOnClickListener {
            handleDebouncedClick {
                Toast.makeText(
                    this,
                    getString(R.string.like_clicked_toast, binding.trackName.text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonAddTrack.setOnClickListener {
            handleDebouncedClick {
                Toast.makeText(
                    this,
                    getString(R.string.add_clicked_toast, binding.trackName.text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun bindTrackData(track: TrackUIModel) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackGenre.text = track.primaryGenreName
        binding.albumName.text = track.collectionName
        binding.trackCountry.text = track.country
        binding.trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.trackYear.text = LocalDateTime.parse(
            track.releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()

        viewModel.loadImage(track.artworkUrl100, binding.playerCover)
    }

    private fun togglePlayPause() {
        viewModel.playPause()
        isPlaying = !isPlaying
        binding.buttonPlay.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
        if (isPlaying) {
            handler.post(updateTimeTask)
        } else {
            handler.removeCallbacks(updateTimeTask)
        }
    }

    private val updateTimeTask: Runnable = object : Runnable {
        override fun run() {
            val currentPosition = viewModel.getCurrentPosition()
            binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseMediaPlayer()
        handler.removeCallbacks(updateTimeTask)
        debounceHandler.removeCallbacks(debounceRunnable)
    }

    private fun handleDebouncedClick(action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceInterval) {
            action()
            lastClickTime = currentTime
            debounceHandler.removeCallbacks(debounceRunnable)
            debounceHandler.postDelayed(debounceRunnable, debounceInterval)
        }
    }

    companion object {
        const val CURRENT_TRACK = "current_track"
    }
}
