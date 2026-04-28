package com.obpiub.runculter.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.obpiub.runculter.databinding.ActivitySplashBinding
import com.obpiub.runculter.ui.auth.AuthActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            navigateToAuth()
        }

        // Автоматический переход через 3 секунды
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToAuth()
        }, 3000)
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
