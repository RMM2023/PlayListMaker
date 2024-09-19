package com.practicum.playlistmaker.data.remote.api

import com.practicum.playlistmaker.data.remote.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): SearchResponse
}
