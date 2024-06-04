package com.practicum.playlistmaker.presentation.ui.UiSettingsActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

const val IS_DARK_THEME = "dark_theme_on"
const val PREF_STATUS = "shared_preferences_status"
class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.themeSwitch.isChecked = (applicationContext as App).isDarkTheme
        binding.themeSwitch.setOnCheckedChangeListener{
            _, isChecked ->
            (applicationContext as App).themeToggle(isChecked)
            val sPref = getSharedPreferences(IS_DARK_THEME, MODE_PRIVATE)
            sPref.edit()
                .putBoolean(PREF_STATUS, isChecked)
                .apply()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.shareButton.setOnClickListener{
            val shareIntent = Intent()
            shareIntent.apply {
                action = Intent.ACTION_SEND
                val courseURL = getString(R.string.course_url)
                val shareText = getString(R.string.share)
                putExtra(Intent.EXTRA_TEXT, courseURL)
                type = "text/plain"
                startActivity(Intent.createChooser(this, shareText))
            }
        }
        binding.supportButton.setOnClickListener{
            val supportIntent = Intent()
            supportIntent.apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_Email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
                startActivity(this)
            }
        }
        binding.agreeButton.setOnClickListener{
            val agreeIntent  = Intent()
            agreeIntent .apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.agreement_uri))
                startActivity(this)
            }
        }
    }

}