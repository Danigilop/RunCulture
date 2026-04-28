package com.obpiub.runculter.ui.achievements.landmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.databinding.FragmentLandmarksBinding
import kotlinx.coroutines.launch

class LandmarksFragment : Fragment() {
    private var _binding: FragmentLandmarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private lateinit var adapter: LandmarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RunCultureApplication).repository

        adapter = LandmarksAdapter { landmark ->
            // Построить маршрут к достопримечательности
            // Здесь можно открыть карту с маршрутом
        }

        binding.landmarksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.landmarksRecyclerView.adapter = adapter

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        loadLandmarks()
    }

    private fun loadLandmarks() {
        lifecycleScope.launch {
            val landmarks = repository.getAllLandmarks()
            adapter.submitList(landmarks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
