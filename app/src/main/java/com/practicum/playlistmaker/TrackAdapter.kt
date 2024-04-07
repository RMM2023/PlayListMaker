package com.practicum.playlistmaker

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

class TrackAdapter():RecyclerView.Adapter<TrackViewHolder>() {
    var trackList:MutableList<Track> = mutableListOf()

    fun updateList(trackList: MutableList<Track>){
        this.trackList = trackList
        notifyDataSetChanged()
    }
    fun clearList(){
        trackList.clear()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

}