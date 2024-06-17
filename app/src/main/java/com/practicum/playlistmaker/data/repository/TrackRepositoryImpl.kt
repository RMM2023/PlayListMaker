package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.remote.api.ITunesApiService
import com.practicum.playlistmaker.domain.model.SearchResult
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRepositoryImpl(private val apiService: ITunesApiService) : TrackRepository {
    override fun searchTracks(term: String, callback: (result: Result<SearchResult>) -> Unit) {
        val call = apiService.search(term)
        call.enqueue(object : Callback<com.practicum.playlistmaker.data.remote.SearchResponse> {
            override fun onResponse(
                call: Call<com.practicum.playlistmaker.data.remote.SearchResponse>,
                response: Response<com.practicum.playlistmaker.data.remote.SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    searchResponse?.let {
                        val searchResult = SearchResult(
                            resultCount = it.resultCount,
                            results = it.results.map { track ->
                                Track(
                                    trackId = track.trackId,
                                    trackName = track.trackName,
                                    artistName = track.artistName,
                                    trackTimeMillis = track.trackTimeMillis,
                                    artworkUrl100 = track.artworkUrl100,
                                    collectionName = track.collectionName,
                                    releaseDate = track.releaseDate,
                                    primaryGenreName = track.primaryGenreName,
                                    country = track.country,
                                    previewUrl = track.previewUrl
                                )
                            }
                        )
                        callback(Result.success(searchResult))
                    } ?: callback(Result.failure(Exception("Search response is null")))
                } else {
                    callback(Result.failure(Exception("Response is not successful")))
                }
            }

            override fun onFailure(call: Call<com.practicum.playlistmaker.data.remote.SearchResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}