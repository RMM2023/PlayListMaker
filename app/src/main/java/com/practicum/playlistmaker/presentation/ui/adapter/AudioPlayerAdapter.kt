package com.practicum.playlistmaker.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist

class AudioPlayerAdapter(private val clickListener: AudioPlayerViewHolder.ClickListener) :
    RecyclerView.Adapter<AudioPlayerViewHolder>() {
    var playlists = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bottom_sheet_playlist, parent, false)
        return AudioPlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        holder.bind(playlists[position], clickListener)
    }
}