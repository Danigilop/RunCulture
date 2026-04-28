package com.obpiub.runculter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmarks")
data class Landmark(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val isVisited: Boolean = false,
    val visitDate: Long? = null
)
