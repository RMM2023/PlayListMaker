package com.practicum.playlistmaker.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.presentation.ui.activity.FavoriteFragment
import com.practicum.playlistmaker.presentation.ui.activity.PlaylistFragment

const val COUNT_TAB = 2

class ViewPagerAdapter(fm : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return COUNT_TAB
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> FavoriteFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}