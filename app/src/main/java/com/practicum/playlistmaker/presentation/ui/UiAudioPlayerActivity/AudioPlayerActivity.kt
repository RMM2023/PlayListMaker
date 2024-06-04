package com.practicum.playlistmaker.presentation.ui.UiAudioPlayerActivity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.practicum.playlistmaker.presentation.ui.UiSearchActivity.CURRENT_TRACK
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.AudioPlayerImplementation
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private var isPlaying = false

    private lateinit var binding: ActivityAudioPlayerBinding

    val audioPlayer = AudioPlayerImplementation()

    private val debounceInterval = 1000L
    private var lastClickTime = 0L
    private val debounceHandler = Handler(Looper.getMainLooper())
    private val debounceRunnable = Runnable {
        lastClickTime = 0L
    }

    private fun getTrack(json: String?): Track? {
        return try {
            Gson().fromJson(json, Track::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra(CURRENT_TRACK)
        track = getTrack(trackJson) ?: run {
            Toast.makeText(this, "Error loading track data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackGenre.text = track.primaryGenreName
        binding.albumName.text = track.collectionName
        binding.trackCountry.text = track.country
        binding.trackLength.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.trackYear.text = LocalDateTime.parse(
            track.releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()

        Glide.with(binding.playerCover)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(15))
            .into(binding.playerCover)

        handler = Handler(Looper.getMainLooper())

        audioPlayer.setupMediaPlayer(binding, track)
        handler.removeCallbacks(updateTimeTask)

        binding.buttonBackPlayer.setOnClickListener { finish() }

        binding.buttonPlay.setOnClickListener { handleDebouncedClick {
            audioPlayer.togglePlayPause(binding, isPlaying)
            if (isPlaying){
                handler.removeCallbacks(updateTimeTask)
            }else{
                handler.post(updateTimeTask)
            }
            isPlaying = !isPlaying
        } }

        binding.buttonLike.setOnClickListener {
            handleDebouncedClick {
                Toast.makeText(
                    this,
                    getString(R.string.like_clicked_toast, track.trackName),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonAddTrack.setOnClickListener {
            handleDebouncedClick {
                Toast.makeText(
                    this,
                    getString(R.string.add_clicked_toast, track.trackName),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    private val updateTimeTask: Runnable = object : Runnable {
        override fun run() {
            val currentPosition = mediaPlayer.currentPosition
            binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
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
}
