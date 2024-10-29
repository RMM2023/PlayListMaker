package com.practicum.playlistmaker.presentation.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.BottomSheetPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist

class AudioPlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = BottomSheetPlaylistBinding.bind(view)

    fun bind(model: Playlist, clickListener: ClickListener) {
        binding.bottomSheetNamePlaylist.text = model.name
        binding.bottomSheetDecsPlaylist.text = setTracksAmount(itemView.context, model.tracksAmount)
        val previewUri = model.imageUri.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(previewUri)
            .placeholder(R.drawable.placeholder)
            .into(binding.bottomSheetCoverPlaylist)
        itemView.setOnClickListener {
            clickListener.onClick(model)
        }
    }

    private fun setTracksAmount(context: Context, count: Int): String {
        val countTrack = context.resources.getQuantityString(R.plurals.track_count, count)
        return "$count $countTrack"
    }

    fun interface ClickListener {
        fun onClick(playlist: Playlist)
    }

}