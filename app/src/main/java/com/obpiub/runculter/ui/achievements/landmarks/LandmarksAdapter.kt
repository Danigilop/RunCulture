package com.obpiub.runculter.ui.achievements.landmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obpiub.runculter.data.model.Landmark
import com.obpiub.runculter.databinding.ItemLandmarkBinding

class LandmarksAdapter(
    private val onItemClick: (Landmark) -> Unit
) : ListAdapter<Landmark, LandmarksAdapter.LandmarkViewHolder>(LandmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkViewHolder {
        val binding = ItemLandmarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LandmarkViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: LandmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LandmarkViewHolder(
        private val binding: ItemLandmarkBinding,
        private val onItemClick: (Landmark) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(landmark: Landmark) {
            binding.landmarkName.text = landmark.name
            binding.landmarkDescription.text = landmark.description
            
            binding.buildRouteButton.setOnClickListener {
                onItemClick(landmark)
            }
        }
    }

    class LandmarkDiffCallback : DiffUtil.ItemCallback<Landmark>() {
        override fun areItemsTheSame(oldItem: Landmark, newItem: Landmark): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Landmark, newItem: Landmark): Boolean {
            return oldItem == newItem
        }
    }
}
