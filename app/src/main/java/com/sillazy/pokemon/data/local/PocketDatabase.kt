package com.sillazy.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sillazy.pokemon.domain.model.Pocket

//로컬 Room DB 선언

@Database(entities = [Pocket::class], version = 1)
abstract class PocketDatabase : RoomDatabase() {
    abstract val pocketDao: PocketDao
}