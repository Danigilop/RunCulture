package com.obpiub.runculter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.obpiub.runculter.data.model.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: Long): Character?

    @Query("SELECT * FROM characters WHERE isUnlocked = 1")
    suspend fun getUnlockedCharacters(): List<Character>

    @Query("SELECT * FROM characters WHERE landmarkId = :landmarkId LIMIT 1")
    suspend fun getCharacterByLandmarkId(landmarkId: Long): Character?

    @Insert
    suspend fun insertCharacter(character: Character): Long

    @Update
    suspend fun updateCharacter(character: Character)
}
