package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.repository.SettingsRepository

class GetUserAgreementLinkUseCase(private val repository: SettingsRepository) {
    fun execute(): String = repository.getUserAgreementLink()
}