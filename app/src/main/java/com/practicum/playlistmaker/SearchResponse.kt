package com.practicum.playlistmaker

data class SearchResponse(val resultCount : Int, val results : MutableList<Track>)