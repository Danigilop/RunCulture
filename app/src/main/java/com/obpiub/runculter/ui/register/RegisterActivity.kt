package com.obpiub.runculter.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.obpiub.runculter.R
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.data.model.User
import com.obpiub.runculter.databinding.ActivityRegisterBinding
import com.obpiub.runculter.ui.main.MainActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as RunCultureApplication).repository

        binding.createAccountButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val fullName = binding.fullNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                Toast.makeText(this@RegisterActivity, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val username = email.substringBefore("@")
            val user = User(
                fullName = fullName,
                username = username,
                email = email,
                password = password
            )

            val userId = repository.register(user)
            if (userId > 0) {
                Toast.makeText(this@RegisterActivity, "Аккаунт успешно создан", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, "Ошибка при создании аккаунта", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
