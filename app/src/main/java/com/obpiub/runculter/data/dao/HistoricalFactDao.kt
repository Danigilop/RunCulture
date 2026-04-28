package com.obpiub.runculter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.obpiub.runculter.data.model.HistoricalFact

@Dao
interface HistoricalFactDao {
    @Query("SELECT * FROM historical_facts")
    suspend fun getAllFacts(): List<HistoricalFact>

    @Query("SELECT * FROM historical_facts WHERE routeId = :routeId")
    suspend fun getFactsByRoute(routeId: Long): List<HistoricalFact>

    @Query("SELECT * FROM historical_facts WHERE isUnlocked = 1")
    suspend fun getUnlockedFacts(): List<HistoricalFact>

    @Insert
    suspend fun insertFact(fact: HistoricalFact): Long

    @Update
    suspend fun updateFact(fact: HistoricalFact)
    
    @Query("UPDATE historical_facts SET isUnlocked = 1, unlockDate = :unlockDate WHERE id = :factId")
    suspend fun unlockFact(factId: Long, unlockDate: Long)
}
