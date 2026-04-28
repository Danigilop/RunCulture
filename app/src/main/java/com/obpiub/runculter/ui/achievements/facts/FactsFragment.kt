package com.obpiub.runculter.ui.achievements.facts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.obpiub.runculter.RunCultureApplication
import com.obpiub.runculter.databinding.FragmentFactsBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FactsFragment : Fragment() {
    private var _binding: FragmentFactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: com.obpiub.runculter.data.repository.AppRepository
    private lateinit var adapter: FactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RunCultureApplication).repository

        adapter = FactsAdapter()

        binding.factsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.factsRecyclerView.adapter = adapter

        loadFacts()
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список фактов при возврате на экран
        loadFacts()
    }

    private fun loadFacts() {
        lifecycleScope.launch {
            // Загружаем все факты для отладки
            val allFacts = repository.getAllFacts()
            Log.d("FactsFragment", "Всего фактов в базе: ${allFacts.size}")
            allFacts.forEach { fact ->
                Log.d("FactsFragment", "Факт: ${fact.title}, разблокирован: ${fact.isUnlocked}, id: ${fact.id}")
            }
            
            // Загружаем только разблокированные через SQL запрос
            val unlockedFactsFromDb = repository.getUnlockedFacts()
            Log.d("FactsFragment", "Загружено разблокированных фактов из БД: ${unlockedFactsFromDb.size}")
            
            // ДОПОЛНИТЕЛЬНАЯ ФИЛЬТРАЦИЯ: Фильтруем вручную, чтобы убедиться, что показываем только разблокированные
            val unlockedFacts = unlockedFactsFromDb.filter { fact ->
                val isUnlocked = fact.isUnlocked == true
                if (!isUnlocked) {
                    Log.w("FactsFragment", "Факт ${fact.title} помечен как разблокированный в БД, но isUnlocked = false, исключаем из списка")
                }
                isUnlocked
            }
            
            Log.d("FactsFragment", "После фильтрации: ${unlockedFacts.size} разблокированных фактов")
            
            if (unlockedFacts.isEmpty() && allFacts.isNotEmpty()) {
                Log.w("FactsFragment", "ВНИМАНИЕ: Есть факты в базе, но ни один не разблокирован!")
                val lockedCount = allFacts.count { !it.isUnlocked }
                val unlockedCount = allFacts.count { it.isUnlocked }
                Log.d("FactsFragment", "Статистика: заблокировано: $lockedCount, разблокировано: $unlockedCount")
            }
            
            // Обновляем список в UI - ТОЛЬКО разблокированные факты
            withContext(kotlinx.coroutines.Dispatchers.Main) {
                adapter.submitList(unlockedFacts)
                if (unlockedFacts.isEmpty()) {
                    Log.w("FactsFragment", "Список фактов пуст, проверьте логи выше")
                } else {
                    Log.d("FactsFragment", "Список фактов обновлен, элементов: ${unlockedFacts.size}")
                    unlockedFacts.forEach { fact ->
                        Log.d("FactsFragment", "Отображаемый факт: ${fact.title}, id: ${fact.id}")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
