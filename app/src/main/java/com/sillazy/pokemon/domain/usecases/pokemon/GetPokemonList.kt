package com.sillazy.pokemon.domain.usecases.pokemon

import androidx.paging.PagingData
import com.sillazy.pokemon.data.model.Result
import com.sillazy.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//포켓몬 전체 리스트를 페이징을 통하여 GET

class GetPokemonList @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke() : Flow<PagingData<Result>> {
        return pokemonRepository.getPokemonList()
    }
}