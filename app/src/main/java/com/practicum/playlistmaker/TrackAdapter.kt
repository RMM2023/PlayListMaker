package com.practicum.playlistmaker

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
const val CURRENT_TRACK = "current_track"
class TrackAdapter(var trackList: MutableList<Track>, private var itemClickListener: (Track) -> Unit):RecyclerView.Adapter<TrackViewHolder>() {

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
        holder.itemView.setOnClickListener{
            itemClickListener(trackList[position])
        }
    }
}