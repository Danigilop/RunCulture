package com.obpiub.runculter.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.obpiub.runculter.R
import com.obpiub.runculter.databinding.FragmentAchievementsBinding
import com.obpiub.runculter.ui.achievements.characters.CharactersFragment
import com.obpiub.runculter.ui.achievements.facts.FactsFragment
import com.obpiub.runculter.ui.achievements.landmarks.LandmarksFragment

class AchievementsFragment : Fragment() {
    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.landmarksButton.setOnClickListener {
            replaceFragment(LandmarksFragment())
        }

        binding.charactersButton.setOnClickListener {
            replaceFragment(CharactersFragment())
        }

        binding.factsButton.setOnClickListener {
            replaceFragment(FactsFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.commit {
            replace(R.id.achievements_content, fragment)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
