package com.obpiub.runculter.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.obpiub.runculter.R
import com.obpiub.runculter.databinding.ActivityMainBinding
import com.obpiub.runculter.ui.achievements.AchievementsFragment
import com.obpiub.runculter.ui.map.MapFragment
import com.obpiub.runculter.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("RunCulturePrefs", MODE_PRIVATE)

        // Проверяем авторизацию
        val userId = sharedPreferences.getLong("userId", -1)
        if (userId == -1L) {
            // Если пользователь не авторизован, возвращаем на экран входа
            finish()
            return
        }

        setupBottomNavigation()
        
        // По умолчанию показываем карту
        if (savedInstanceState == null) {
            replaceFragment(MapFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.nav_map -> {
                    replaceFragment(MapFragment())
                    true
                }
                R.id.nav_achievements -> {
                    replaceFragment(AchievementsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    fun navigateToAchievements() {
        binding.bottomNavigation.selectedItemId = R.id.nav_achievements
        replaceFragment(AchievementsFragment())
    }
}
