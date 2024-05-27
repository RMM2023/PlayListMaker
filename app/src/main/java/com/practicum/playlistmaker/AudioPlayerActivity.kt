package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private var isPlaying = false

    private lateinit var playPauseButton: ImageView
    private lateinit var playerTime: TextView

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
        setContentView(R.layout.activity_audio_player)

        playerTime = findViewById(R.id.player_time)
        val backButton: Button = findViewById(R.id.button_back_player)
        val addToPlayList: ImageView = findViewById(R.id.button_add_track)
        playPauseButton = findViewById(R.id.button_play)
        val likeButton: ImageView = findViewById(R.id.button_like)

        val trackCover = findViewById<ImageView>(R.id.player_cover)
        val trackName = findViewById<TextView>(R.id.track_name)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val trackLength = findViewById<TextView>(R.id.track_length)
        val trackGenre = findViewById<TextView>(R.id.track_genre)
        val trackCountry = findViewById<TextView>(R.id.track_country)
        val trackYear = findViewById<TextView>(R.id.track_year)
        val albumName = findViewById<TextView>(R.id.album_name)

        val trackJson = intent.getStringExtra(CURRENT_TRACK)
        track = getTrack(trackJson) ?: run {
            Toast.makeText(this, "Error loading track data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackGenre.text = track.primaryGenreName
        albumName.text = track.collectionName
        trackCountry.text = track.country
        trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackYear.text = LocalDateTime.parse(track.releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).year.toString()

        Glide.with(trackCover)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(15))
            .into(trackCover)

        handler = Handler(Looper.getMainLooper())

        setupMediaPlayer()

        backButton.setOnClickListener { finish() }

        playPauseButton.setOnClickListener { togglePlayPause() }

        likeButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.like_clicked_toast, track.trackName), Toast.LENGTH_SHORT).show()
        }

        addToPlayList.setOnClickListener {
            Toast.makeText(this, getString(R.string.add_clicked_toast, track.trackName), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.previewUrl)
            setOnPreparedListener {
                playerTime.text = "00:00"
                playPauseButton.setImageResource(R.drawable.play)
            }
            prepareAsync()
            setOnCompletionListener {

                playPauseButton.setImageResource(R.drawable.play)
                playerTime.text = "00:00"
                handler.removeCallbacks(updateTimeTask)
            }
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause()
            playPauseButton.setImageResource(R.drawable.play)
            handler.removeCallbacks(updateTimeTask)
        } else {
            mediaPlayer.start()
            playPauseButton.setImageResource(R.drawable.pause)
            handler.post(updateTimeTask)
        }
        isPlaying = !isPlaying
    }

    private val updateTimeTask: Runnable = object : Runnable {
        override fun run() {
            val currentPosition = mediaPlayer.currentPosition
            playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimeTask)
    }
}
