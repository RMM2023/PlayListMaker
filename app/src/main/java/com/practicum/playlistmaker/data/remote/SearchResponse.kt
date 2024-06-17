package com.practicum.playlistmaker.data.remote

import com.practicum.playlistmaker.data.model.TrackEntity

data class SearchResponse(val resultCount : Int, val results : MutableList<TrackEntity>)