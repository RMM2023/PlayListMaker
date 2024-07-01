package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.model.ThemeSettings
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class SetThemeSettingsUseCase(private val repository: SettingsRepository) {
    fun execute(settings: ThemeSettings) = repository.setThemeSettings(settings)
}