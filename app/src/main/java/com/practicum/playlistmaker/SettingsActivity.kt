package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
        val shareButton = findViewById<Button>(R.id.share_button)
        shareButton.setOnClickListener{
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
        val supportButton = findViewById<Button>(R.id.support_button)
        supportButton.setOnClickListener{
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
        val agreeButton = findViewById<Button>(R.id.agree_button)
        agreeButton.setOnClickListener{
            val agreeIntent  = Intent()
            agreeIntent .apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.agreement_uri))
                startActivity(this)
            }
        }
    }

}