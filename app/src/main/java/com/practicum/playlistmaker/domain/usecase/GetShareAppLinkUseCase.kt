package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.repository.SettingsRepository

class GetShareAppLinkUseCase(private val repository: SettingsRepository) {
    fun execute(): String = repository.getShareAppLink()
}