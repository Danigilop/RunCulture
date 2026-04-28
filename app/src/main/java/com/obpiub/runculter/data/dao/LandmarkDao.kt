package com.obpiub.runculter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.obpiub.runculter.data.model.Landmark

@Dao
interface LandmarkDao {
    @Query("SELECT * FROM landmarks")
    suspend fun getAllLandmarks(): List<Landmark>

    @Query("SELECT * FROM landmarks WHERE id = :id LIMIT 1")
    suspend fun getLandmarkById(id: Long): Landmark?

    @Query("SELECT * FROM landmarks WHERE isVisited = 1")
    suspend fun getVisitedLandmarks(): List<Landmark>

    @Insert
    suspend fun insertLandmark(landmark: Landmark): Long

    @Update
    suspend fun updateLandmark(landmark: Landmark)
}
