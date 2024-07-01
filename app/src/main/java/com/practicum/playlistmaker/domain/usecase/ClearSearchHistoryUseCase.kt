package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    fun execute() = repository.clearSearchHistory()
}