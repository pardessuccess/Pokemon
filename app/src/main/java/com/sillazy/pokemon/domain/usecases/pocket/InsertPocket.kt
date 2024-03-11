package com.sillazy.pokemon.domain.usecases.pocket

import com.sillazy.pokemon.data.local.PocketDao
import com.sillazy.pokemon.domain.model.Pocket
import javax.inject.Inject

//룸 디비 포켓몬 삽입

class InsertPocket @Inject constructor(
    private val pocketDao: PocketDao,
) {
    suspend operator fun invoke(pocket: Pocket) {
        pocketDao.insert(pocket)
    }
}