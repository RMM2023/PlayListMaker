package com.practicum.playlistmaker.presentation.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.CURRENT_TRACK
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.practicum.playlistmaker.presentation.viewmodel.AudioPlayerViewModelFactory

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra(CURRENT_TRACK)
        val track = getTrack(trackJson) ?: run {
            Toast.makeText(this, "Error loading track data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
        viewModel = ViewModelProvider(this, AudioPlayerViewModelFactory(mediaPlayerInteractor))
            .get(AudioPlayerViewModel::class.java)

        viewModel.initTrack(track)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.buttonBackPlayer.setOnClickListener { finish() }
        binding.buttonPlay.setOnClickListener { viewModel.togglePlayPause() }
        binding.buttonLike.setOnClickListener { likeTrack() }
        binding.buttonAddTrack.setOnClickListener { addTrack() }

        // Устанавливаем начальное состояние кнопки воспроизведения
        binding.buttonPlay.setImageResource(R.drawable.play)
    }

    private fun observeViewModel() {
        viewModel.track.observe(this) { track ->
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackGenre.text = track.primaryGenreName
            binding.albumName.text = track.collectionName
            binding.trackCountry.text = track.country
            binding.trackLength.text = viewModel.formatTrackTime(track.trackTimeMillis)
            binding.trackYear.text = viewModel.formatYear(track.releaseDate)

            Glide.with(binding.playerCover)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(15))
                .into(binding.playerCover)
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            binding.buttonPlay.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
        }

        viewModel.currentPosition.observe(this) { position ->
            binding.playerTime.text = position
        }
    }

    private fun getTrack(json: String?): Track? {
        return try {
            Gson().fromJson(json, Track::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    private fun likeTrack() {
        viewModel.track.value?.let { track ->
            Toast.makeText(this, getString(R.string.like_clicked_toast, track.trackName), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addTrack() {
        viewModel.track.value?.let { track ->
            Toast.makeText(this, getString(R.string.add_clicked_toast, track.trackName), Toast.LENGTH_SHORT).show()
        }
    }
}