package com.obpiub.runculter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.obpiub.runculter.data.database.Converters

@Entity(tableName = "characters")
@TypeConverters(Converters::class)
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val imageUrl: String? = null,
    val isUnlocked: Boolean = false,
    val unlockDate: Long? = null,
    val landmarkId: Long? = null,
    val dialogues: List<String> = emptyList()
)
