package com.practicum.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R

class TrackViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val trackImage = itemView.findViewById<ImageView>(R.id.track_image)
    val trackTitle = itemView.findViewById<TextView>(R.id.track_title)
    val trackArtist = itemView.findViewById<TextView>(R.id.track_artist)
    val trackTime = itemView.findViewById<TextView>(R.id.track_time)

    fun bind(track: Track){
        Glide.with(trackImage).load(track.artworkUrl100)
            .centerInside()
            .transform(RoundedCorners(4))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
        trackTitle.text = track.trackName
        trackArtist.text = track.artistName
        trackTime.text = track.trackTime
        itemView.setOnClickListener{
            Toast.makeText(itemView.context, "Не реализовано", Toast.LENGTH_SHORT).show()
        }
    }
}