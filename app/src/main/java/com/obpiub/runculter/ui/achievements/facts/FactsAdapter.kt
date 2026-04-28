package com.obpiub.runculter.ui.achievements.facts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obpiub.runculter.data.model.HistoricalFact
import com.obpiub.runculter.databinding.ItemFactBinding

class FactsAdapter : ListAdapter<HistoricalFact, FactsAdapter.FactViewHolder>(FactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val binding = ItemFactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FactViewHolder(
        private val binding: ItemFactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(fact: HistoricalFact) {
            binding.factTitle.text = fact.title
            binding.factContent.text = fact.content
        }
    }

    class FactDiffCallback : DiffUtil.ItemCallback<HistoricalFact>() {
        override fun areItemsTheSame(oldItem: HistoricalFact, newItem: HistoricalFact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoricalFact, newItem: HistoricalFact): Boolean {
            return oldItem == newItem
        }
    }
}
