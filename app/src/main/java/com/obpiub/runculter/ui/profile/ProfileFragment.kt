package com.obpiub.runculter.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.databinding.FragmentProfileBinding
import com.obpiub.runculter.ui.auth.AuthActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RunCultureApplication).repository
        sharedPreferences = requireActivity().getSharedPreferences("RunCulturePrefs", android.content.Context.MODE_PRIVATE)

        loadUserData()

        binding.saveButton.setOnClickListener {
            saveUserData()
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun loadUserData() {
        val userId = sharedPreferences.getLong("userId", -1)
        if (userId != -1L) {
            lifecycleScope.launch {
                val user = repository.getUserById(userId)
                user?.let {
                    binding.fullNameEditText.setText(it.fullName)
                    binding.usernameEditText.setText(it.username)
                    binding.emailEditText.setText(it.email)
                }
            }
        }
    }

    private fun saveUserData() {
        val userId = sharedPreferences.getLong("userId", -1)
        if (userId == -1L) return

        val fullName = binding.fullNameEditText.text.toString().trim()
        val username = binding.usernameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = repository.getUserById(userId)
            user?.let {
                val updatedUser = it.copy(
                    fullName = fullName,
                    username = username,
                    email = email
                )
                repository.updateUser(updatedUser)
                Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        sharedPreferences.edit().remove("userId").apply()
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
