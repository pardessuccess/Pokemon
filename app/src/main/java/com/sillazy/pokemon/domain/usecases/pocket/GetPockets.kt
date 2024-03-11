package com.sillazy.pokemon.domain.usecases.pocket

import com.sillazy.pokemon.data.local.PocketDao
import com.sillazy.pokemon.domain.model.Pocket
import com.sillazy.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//룸 디비 포켓몬 갱신

class GetPockets @Inject constructor(
    private val pocketDao: PocketDao
) {
    operator fun invoke(): Flow<List<Pocket>> {
        return pocketDao.getPockets()
    }

}