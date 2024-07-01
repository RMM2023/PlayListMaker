package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.repository.SettingsRepository

class GetSupportEmailDataUseCase(private val repository: SettingsRepository) {
    fun execute(): Triple<String, String, String> = repository.getSupportEmailData()
}