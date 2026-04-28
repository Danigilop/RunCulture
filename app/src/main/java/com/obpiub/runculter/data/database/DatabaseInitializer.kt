package com.obpiub.runculter.data.database

import android.util.Log
import com.obpiub.runculter.data.model.Character
import com.obpiub.runculter.data.model.HistoricalFact
import com.obpiub.runculter.data.model.Landmark
import com.obpiub.runculter.data.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Класс для инициализации данных в базе данных
 * 
 * ВАЖНО: Все достопримечательности добавляются в файле LandmarksData.kt
 * Откройте LandmarksData.kt и добавьте свои достопримечательности там!
 */
class DatabaseInitializer(private val repository: AppRepository) {

    /**
     * Инициализирует данные из LandmarksData.kt
     * Вызывается автоматически при первом запуске приложения
     */
    fun initializeTestData() {
        CoroutineScope(Dispatchers.IO).launch {
            // Проверяем, есть ли уже данные
            val existingLandmarks = repository.getAllLandmarks()
            Log.d("DatabaseInitializer", "Существующих достопримечательностей: ${existingLandmarks.size}")
            
            if (existingLandmarks.isNotEmpty()) {
                // Проверяем, есть ли достопримечательность с Ралиной
                val hasRalina = existingLandmarks.any { 
                    kotlin.math.abs(it.latitude - 37.421982) < 0.0001 && 
                    kotlin.math.abs(it.longitude - (-122.084796)) < 0.0001
                }
                Log.d("DatabaseInitializer", "Достопримечательность с Ралиной найдена: $hasRalina")
                
                if (!hasRalina) {
                    // Если данных нет, но база не пустая, добавляем только недостающие
                    Log.d("DatabaseInitializer", "Добавляем недостающие достопримечательности...")
                    addMissingLandmarks()
                }
                return@launch
            }

            // Загружаем достопримечательности из LandmarksData.kt
            val landmarksData = LandmarksData.getLandmarks()
            Log.d("DatabaseInitializer", "Загружаем ${landmarksData.size} достопримечательностей из LandmarksData")
            
            landmarksData.forEach { data ->
                // Добавляем достопримечательность
                val landmarkId = repository.insertLandmark(data.landmark)
                Log.d("DatabaseInitializer", "Добавлена достопримечательность: ${data.landmark.name} (ID: $landmarkId) - ${data.landmark.latitude}, ${data.landmark.longitude}")
                
                // Добавляем персонажа, связанного с достопримечательностью
                val character = data.character.copy(landmarkId = landmarkId)
                repository.insertCharacter(character)
                
                // Добавляем исторические факты, связывая их с достопримечательностью через routeId = landmarkId
                data.facts.forEach { fact ->
                    // Устанавливаем routeId = landmarkId, чтобы связать факт с достопримечательностью
                    val factWithLandmarkId = fact.copy(routeId = landmarkId, isUnlocked = false)
                    repository.insertFact(factWithLandmarkId)
                    Log.d("DatabaseInitializer", "Добавлен факт: ${fact.title} для достопримечательности ID: $landmarkId")
                }
            }
        }
    }
    
    /**
     * Добавляет недостающие достопримечательности (например, если база была создана до добавления новых данных)
     */
    private suspend fun addMissingLandmarks() {
        val landmarksData = LandmarksData.getLandmarks()
        val existingLandmarks = repository.getAllLandmarks()
        
        landmarksData.forEach { data ->
            // Проверяем, есть ли уже такая достопримечательность
            val exists = existingLandmarks.any { 
                it.latitude == data.landmark.latitude && 
                it.longitude == data.landmark.longitude 
            }
            
            if (!exists) {
                val landmarkId = repository.insertLandmark(data.landmark)
                Log.d("DatabaseInitializer", "Добавлена недостающая достопримечательность: ${data.landmark.name} - ${data.landmark.latitude}, ${data.landmark.longitude}")
                
                val character = data.character.copy(landmarkId = landmarkId)
                repository.insertCharacter(character)
                
                // Добавляем исторические факты, связывая их с достопримечательностью через routeId = landmarkId
                data.facts.forEach { fact ->
                    // Устанавливаем routeId = landmarkId, чтобы связать факт с достопримечательностью
                    val factWithLandmarkId = fact.copy(routeId = landmarkId, isUnlocked = false)
                    repository.insertFact(factWithLandmarkId)
                    Log.d("DatabaseInitializer", "Добавлен недостающий факт: ${fact.title} для достопримечательности ID: $landmarkId")
                }
            }
        }
    }

    /**
     * Добавляет новую достопримечательность
     * Используйте этот метод для добавления своих достопримечательностей
     */
    suspend fun addLandmark(
        name: String,
        description: String,
        latitude: Double,
        longitude: Double,
        characterName: String? = null,
        characterDescription: String? = null,
        characterDialogues: List<String> = emptyList()
    ): Long {
        // Создаем достопримечательность
        val landmark = Landmark(
            name = name,
            description = description,
            latitude = latitude,
            longitude = longitude,
            isVisited = false
        )
        val landmarkId = repository.insertLandmark(landmark)

        // Если указан персонаж, создаем его
        if (characterName != null && characterDescription != null) {
            val character = Character(
                name = characterName,
                description = characterDescription,
                landmarkId = landmarkId,
                dialogues = characterDialogues,
                isUnlocked = false
            )
            repository.insertCharacter(character)
        }

        return landmarkId
    }
}
