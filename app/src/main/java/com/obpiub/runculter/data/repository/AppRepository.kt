package com.obpiub.runculter.data.repository

import com.obpiub.runculter.data.dao.CharacterDao
import com.obpiub.runculter.data.dao.HistoricalFactDao
import com.obpiub.runculter.data.dao.LandmarkDao
import com.obpiub.runculter.data.dao.UserDao
import com.obpiub.runculter.data.model.Character
import com.obpiub.runculter.data.model.HistoricalFact
import com.obpiub.runculter.data.model.Landmark
import com.obpiub.runculter.data.model.User

class AppRepository(
    private val userDao: UserDao,
    private val landmarkDao: LandmarkDao,
    private val characterDao: CharacterDao,
    private val historicalFactDao: HistoricalFactDao
) {
    // User operations
    suspend fun login(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun register(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserById(id: Long): User? {
        return userDao.getUserById(id)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    // Landmark operations
    suspend fun getAllLandmarks(): List<Landmark> {
        return landmarkDao.getAllLandmarks()
    }

    suspend fun getLandmarkById(id: Long): Landmark? {
        return landmarkDao.getLandmarkById(id)
    }

    suspend fun getVisitedLandmarks(): List<Landmark> {
        return landmarkDao.getVisitedLandmarks()
    }

    suspend fun insertLandmark(landmark: Landmark): Long {
        return landmarkDao.insertLandmark(landmark)
    }

    suspend fun updateLandmark(landmark: Landmark) {
        landmarkDao.updateLandmark(landmark)
    }

    // Character operations
    suspend fun getAllCharacters(): List<Character> {
        return characterDao.getAllCharacters()
    }

    suspend fun getCharacterById(id: Long): Character? {
        return characterDao.getCharacterById(id)
    }

    suspend fun getUnlockedCharacters(): List<Character> {
        return characterDao.getUnlockedCharacters()
    }

    suspend fun getCharacterByLandmarkId(landmarkId: Long): Character? {
        return characterDao.getCharacterByLandmarkId(landmarkId)
    }

    suspend fun insertCharacter(character: Character): Long {
        return characterDao.insertCharacter(character)
    }

    suspend fun updateCharacter(character: Character) {
        characterDao.updateCharacter(character)
    }

    // Historical Fact operations
    suspend fun getAllFacts(): List<HistoricalFact> {
        return historicalFactDao.getAllFacts()
    }

    suspend fun getFactsByRoute(routeId: Long): List<HistoricalFact> {
        return historicalFactDao.getFactsByRoute(routeId)
    }

    suspend fun getUnlockedFacts(): List<HistoricalFact> {
        return historicalFactDao.getUnlockedFacts()
    }

    suspend fun insertFact(fact: HistoricalFact): Long {
        return historicalFactDao.insertFact(fact)
    }

    suspend fun updateFact(fact: HistoricalFact) {
        historicalFactDao.updateFact(fact)
    }
    
    suspend fun unlockFact(factId: Long, unlockDate: Long) {
        android.util.Log.d("AppRepository", "unlockFact вызван: factId=$factId, unlockDate=$unlockDate")
        try {
            historicalFactDao.unlockFact(factId, unlockDate)
            android.util.Log.d("AppRepository", "✓ unlockFact выполнен успешно для factId=$factId")
        } catch (e: Exception) {
            android.util.Log.e("AppRepository", "ОШИБКА в unlockFact для factId=$factId: ${e.message}", e)
            throw e
        }
    }
}
