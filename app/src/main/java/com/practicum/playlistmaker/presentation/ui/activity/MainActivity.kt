package com.practicum.playlistmaker.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding

interface NavigationBarController {
    fun hideNavBar()
    fun showNavBar()
}

class MainActivity : AppCompatActivity(), NavigationBarController {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationBar
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun showNavBar() {
        if (::binding.isInitialized) {
            binding.bottomNavigationBar.visibility = View.VISIBLE
        }
    }

    override fun hideNavBar() {
        if (::binding.isInitialized) {
            binding.bottomNavigationBar.visibility = View.GONE
        }
    }

}