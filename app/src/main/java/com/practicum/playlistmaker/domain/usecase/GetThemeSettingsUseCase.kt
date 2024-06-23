package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.model.ThemeSettings
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class GetThemeSettingsUseCase(private val repository: SettingsRepository) {
    fun execute(): ThemeSettings = repository.getThemeSettings()
}