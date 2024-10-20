package com.practicum.playlistmaker.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.CURRENT_TRACK
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.ui.adapter.PlaylistsAdapter
import com.practicum.playlistmaker.presentation.ui.adapter.PlaylistsViewHolder
import com.practicum.playlistmaker.presentation.ui.fragments.NewPlaylistFragment
import com.practicum.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        viewModel.initTrack(track)

        setupUI()
        observeViewModel()
        setupBottomSheet()
        setupPlaylistsRecyclerView()
    }

    private fun setupUI() {
        binding.buttonBackPlayer.setOnClickListener { finish() }
        binding.buttonPlay.setOnClickListener { viewModel.togglePlayPause() }
        binding.buttonLike.setOnClickListener { likeTrack() }
        binding.buttonAddTrack.setOnClickListener { showBottomSheet() }

        // Устанавливаем начальное состояние кнопки воспроизведения
        binding.buttonPlay.setImageResource(R.drawable.play)
        binding.buttonLike.setOnClickListener{viewModel.onFavoriteClicked()}
    }

    private fun observeViewModel() {
        viewModel.track.observe(this) { track ->
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackGenre.text = track.primaryGenreName
            binding.albumName.text = track.collectionName
            binding.trackCountry.text = track.country
            binding.trackLength.text = track.trackTimeMillis?.let { viewModel.formatTrackTime(it) }
            binding.trackYear.text = track.releaseDate?.let { viewModel.formatYear(it) }

            Glide.with(binding.playerCover)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
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
        viewModel.isFavorite.observe(this){isFavorite ->
            binding.buttonLike.setImageResource(
                if (isFavorite) R.drawable.like_pressed
                else R.drawable.like
            )
        }
        viewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetAudioPlayer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.bottomSheetAddPlaylistBtn.setOnClickListener {
            showNewPlaylistFragment()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showNewPlaylistFragment() {
        val newPlaylistFragment = NewPlaylistFragment()
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, newPlaylistFragment)
            .addToBackStack(null)
            .commit()
        hideBottomSheet()
    }


    private fun setupPlaylistsRecyclerView() {
        val adapter = PlaylistsAdapter(object : PlaylistsViewHolder.ClickListener {
            override fun onClick(playlist: Playlist) {
                viewModel.addTrackToPlaylist(playlist)
                hideBottomSheet()
            }
        })
        binding.bottomSheetRecyclerView.adapter = adapter
        binding.bottomSheetRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.playlists.observe(this) { playlists ->
            adapter.playlists = ArrayList(playlists)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheetOverlay.visibility = View.VISIBLE
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.bottomSheetOverlay.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_PLAYLIST_REQUEST_CODE && resultCode == RESULT_OK) {
            viewModel.refreshPlaylists()
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


    companion object {
        private const val NEW_PLAYLIST_REQUEST_CODE = 1
    }
}