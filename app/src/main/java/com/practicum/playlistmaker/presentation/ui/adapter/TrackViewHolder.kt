package com.practicum.playlistmaker.presentation.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImage = itemView.findViewById<ImageView>(R.id.track_image)
    private val trackTitle = itemView.findViewById<TextView>(R.id.track_title)
    private val trackArtist = itemView.findViewById<TextView>(R.id.track_artist)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)

    fun bind(track: Track) {
        if (track.artworkUrl100?.isNotEmpty() == true) {
            Glide.with(trackImage)
                .load(track.artworkUrl100)
                .centerInside()
                .transform(RoundedCorners(4))
                .placeholder(R.drawable.placeholder)
                .into(trackImage)
        } else {
            trackImage.setImageResource(R.drawable.placeholder)
        }
        trackTitle.text = track.trackName
        trackArtist.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }
}