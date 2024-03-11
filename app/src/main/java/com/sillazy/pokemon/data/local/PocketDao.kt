package com.sillazy.pokemon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sillazy.pokemon.domain.model.Pocket
import kotlinx.coroutines.flow.Flow

//로컬 Room DB에 저장하는 Dao입니다

@Dao
interface PocketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pocket: Pocket)

    @Delete
    suspend fun delete(pocket: Pocket)

    @Query("SELECT * FROM Pocket")
    fun getPockets() : Flow<List<Pocket>>


}