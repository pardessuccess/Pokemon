package com.sillazy.pokemon.domain.usecases.pokemon

import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.data.model.PokemonListDto
import com.sillazy.pokemon.domain.repository.PokemonRepository
import javax.inject.Inject

//포켓몬 전체 리스트 GET

class GetPokemonListAtOnce @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(): Resource<PokemonListDto> {
        return pokemonRepository.getPokemonListAtOnce()
    }
}