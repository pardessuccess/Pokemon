package com.sillazy.pokemon.domain.usecases.pocket

import com.sillazy.pokemon.data.local.PocketDao
import com.sillazy.pokemon.domain.model.Pocket
import javax.inject.Inject

//룸 디비 포켓몬 삭제

class DeletePocket @Inject constructor(
    private val pocketDao: PocketDao
) {
    suspend operator fun invoke(pocket: Pocket) {
        pocketDao.delete(pocket)
    }
}