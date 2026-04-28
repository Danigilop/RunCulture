package com.obpiub.runculter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.obpiub.runculter.data.dao.CharacterDao
import com.obpiub.runculter.data.dao.HistoricalFactDao
import com.obpiub.runculter.data.dao.LandmarkDao
import com.obpiub.runculter.data.dao.UserDao
import com.obpiub.runculter.data.model.Character
import com.obpiub.runculter.data.model.HistoricalFact
import com.obpiub.runculter.data.model.Landmark
import com.obpiub.runculter.data.model.User

@Database(
    entities = [User::class, Landmark::class, Character::class, HistoricalFact::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RunCultureDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun landmarkDao(): LandmarkDao
    abstract fun characterDao(): CharacterDao
    abstract fun historicalFactDao(): HistoricalFactDao
}
