package com.obpiub.runculter.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.databinding.ActivityLoginBinding
import com.obpiub.runculter.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as RunCultureApplication).repository
        sharedPreferences = getSharedPreferences("RunCulturePrefs", MODE_PRIVATE)

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            Toast.makeText(this, "Функция восстановления пароля в разработке", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = repository.login(email, password)
            if (user != null) {
                // Сохраняем ID пользователя
                sharedPreferences.edit().putLong("userId", user.id).apply()
                
                Toast.makeText(this@LoginActivity, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("userId", user.id)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
