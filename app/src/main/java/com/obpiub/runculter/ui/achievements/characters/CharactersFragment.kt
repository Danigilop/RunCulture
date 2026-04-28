package com.obpiub.runculter.ui.achievements.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.databinding.FragmentCharactersBinding
import kotlinx.coroutines.launch

class CharactersFragment : Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private lateinit var adapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RunCultureApplication).repository

        adapter = CharactersAdapter { character ->
            showCharacterDialog(character)
        }

        binding.charactersRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.charactersRecyclerView.adapter = adapter

        loadCharacters()
    }

    private fun loadCharacters() {
        lifecycleScope.launch {
            val characters = repository.getUnlockedCharacters()
            adapter.submitList(characters)
        }
    }

    private fun showCharacterDialog(character: com.obpiub.runculter.data.model.Character) {
        // Здесь будет диалог с персонажем и его репликами
        CharacterDialogFragment.newInstance(character.id)
            .show(parentFragmentManager, "character_dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
