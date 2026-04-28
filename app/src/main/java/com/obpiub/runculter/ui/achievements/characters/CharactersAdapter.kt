package com.obpiub.runculter.ui.achievements.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obpiub.runculter.data.model.Character
import com.obpiub.runculter.databinding.ItemCharacterBinding

class CharactersAdapter(
    private val onItemClick: (Character) -> Unit
) : ListAdapter<Character, CharactersAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private val onItemClick: (Character) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.characterName.text = character.name
            binding.root.setOnClickListener {
                onItemClick(character)
            }
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}
