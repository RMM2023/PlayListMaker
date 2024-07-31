package com.practicum.playlistmaker.presentation.ui.activity

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.presentation.ui.adapter.ViewPagerAdapter

class MediaActivity : AppCompatActivity() {
     lateinit var binding : ActivityMediaBinding
     lateinit var tabMediator : TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager2.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2){
            tab, position ->
            when (position){
                0 -> tab.text = "Избранные треки"
                else -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
        binding.mediaBackButton.setOnClickListener{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}