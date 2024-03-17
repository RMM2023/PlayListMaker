package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TrackViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val trackImage = itemView.findViewById<ImageView>(R.id.track_image)
    val trackTitle = itemView.findViewById<TextView>(R.id.track_title)
    val trackArtist = itemView.findViewById<TextView>(R.id.track_artist)
    val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    val trackButton = itemView.findViewById<ImageButton>(R.id.track_button)
    fun bind(track: Track){
        trackTitle.text = track.trackName
        trackArtist.text = track.artistName
        trackTime.text = track.trackTime
        itemView.setOnClickListener{

        }
    }
}