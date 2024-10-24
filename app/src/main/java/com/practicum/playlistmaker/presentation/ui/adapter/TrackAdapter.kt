package com.practicum.playlistmaker.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track

class TrackAdapter(
    var trackList: MutableList<Track>,
    var itemClickListener: ((Track) -> Unit)? = null,
) : RecyclerView.Adapter<TrackViewHolder>() {

    lateinit var itemLongClickListener: ((Track) -> Unit)

    fun updateList(trackList: MutableList<Track>) {
        this.trackList = trackList
        notifyDataSetChanged()
    }

    fun clearList() {
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
        holder.itemView.setOnClickListener {
            itemClickListener?.let { it1 -> it1(trackList[position]) }
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.invoke(trackList[position])
            true
        }
    }
}