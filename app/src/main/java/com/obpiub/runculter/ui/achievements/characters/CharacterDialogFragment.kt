package com.obpiub.runculter.ui.achievements.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.databinding.DialogCharacterBinding
import kotlinx.coroutines.launch

class CharacterDialogFragment : DialogFragment() {
    private var _binding: DialogCharacterBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private var characterId: Long = -1

    companion object {
        fun newInstance(characterId: Long): CharacterDialogFragment {
            val fragment = CharacterDialogFragment()
            fragment.arguments = Bundle().apply {
                putLong("characterId", characterId)
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterId = arguments?.getLong("characterId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RunCultureApplication).repository

        loadCharacter()

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun loadCharacter() {
        if (characterId == -1L) return

        lifecycleScope.launch {
            val character = repository.getCharacterById(characterId)
            character?.let {
                binding.characterName.text = it.name
                binding.characterDescription.text = it.description
                // Здесь можно добавить отображение диалогов
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
