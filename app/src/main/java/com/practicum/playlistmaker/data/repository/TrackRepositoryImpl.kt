package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.remote.api.ITunesApiService
import com.practicum.playlistmaker.domain.model.SearchResult
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date


class TrackRepositoryImpl(private val apiService: ITunesApiService) : TrackRepository {
    override fun searchTracks(term: String) : Flow<SearchResult> = flow {
        try{
            val response = apiService.search(term)
            val searchResult = SearchResult(
                resultCount = response.resultCount,
                results = response.results.map{
                        track -> Track(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl,
                    insertTime = Date().time
                )
                }
            )
            emit(searchResult)
        }catch (e : Exception){
            throw e
        }
    }
}