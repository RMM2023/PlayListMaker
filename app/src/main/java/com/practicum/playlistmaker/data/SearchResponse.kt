package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.models.Track

data class SearchResponse(val resultCount : Int, val results : MutableList<Track>)