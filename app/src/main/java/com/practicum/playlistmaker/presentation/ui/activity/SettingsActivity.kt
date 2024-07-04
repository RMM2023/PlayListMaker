package com.practicum.playlistmaker.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.buttonBack.setOnClickListener { finish() }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
            (applicationContext as App).themeToggle(isChecked)
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, viewModel.getShareAppLink())
            }
            startActivity(Intent.createChooser(shareIntent, "Share app"))
        }

        binding.supportButton.setOnClickListener {
            val (email, subject, body) = viewModel.getSupportEmailData()
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(supportIntent)
        }

        binding.agreeButton.setOnClickListener {
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getUserAgreementLink()))
            startActivity(agreeIntent)
        }
    }

    private fun observeViewModel() {
        viewModel.themeSettings.observe(this) { settings ->
            binding.themeSwitch.isChecked = settings.isDarkTheme
        }
    }
}