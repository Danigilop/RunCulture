package com.obpiub.runculter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historical_facts")
data class HistoricalFact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val routeId: Long,
    val imageUrl: String? = null,
    val isUnlocked: Boolean = false,
    val unlockDate: Long? = null
)
