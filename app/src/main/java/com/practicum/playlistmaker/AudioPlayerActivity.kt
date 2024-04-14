package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private var playerTime: TextView? = null
    private lateinit var track: Track
    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        playerTime = findViewById(R.id.player_time)
        val backButton: Button = findViewById(R.id.button_back_player)
        val addToPlayList: ImageView = findViewById(R.id.button_add_track)
        val playPause: ImageView = findViewById(R.id.button_play)
        val likeButton: ImageView = findViewById(R.id.button_like)

        val trackCover = findViewById<ImageView>(R.id.player_cover)
        val trackName = findViewById<TextView>(R.id.track_name)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val trackLenght = findViewById<TextView>(R.id.track_time)
        val trackGenre = findViewById<TextView>(R.id.track_genre)
        val playerTime = findViewById<TextView>(R.id.player_time)
        val trackCountry = findViewById<TextView>(R.id.track_country)
        val trackYear = findViewById<TextView>(R.id.track_year)
        val albumName = findViewById<TextView>(R.id.album_name)

        track = getTrack(intent.getStringExtra(CURRENT_TRACK))
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackGenre.text = track.primaryGenreName
        albumName.text = track.collectionName
        trackCountry.text = track.country
        playerTime.text = "00:01"
        trackLenght.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackYear.text = LocalDateTime.parse(
            track.releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()

        Glide.with(trackCover)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(15))
            .into(trackCover)



        backButton.setOnClickListener {
            finish()
        }

        playPause.setOnClickListener {
            Toast.makeText(this, "Кнопка Play нажата", Toast.LENGTH_SHORT).show()
        }

        likeButton.setOnClickListener {
            Toast.makeText(this, "${track.trackName} добавлена в favorite", Toast.LENGTH_SHORT)
                .show()
        }

        addToPlayList.setOnClickListener {
            Toast.makeText(this, "${track.trackName} Добавлен в Плейлист", Toast.LENGTH_SHORT)
                .show()
        }
    }
}