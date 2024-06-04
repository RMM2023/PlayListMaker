package com.practicum.playlistmaker.data

data class SearchResponse(val resultCount : Int, val results : MutableList<Track>)