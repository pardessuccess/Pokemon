package com.sillazy.pokemon.domain.usecases.pokemon

import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.domain.repository.PokemonRepository
import javax.inject.Inject

//이름 또는 번호로 포켓몬 단일 객체를 GET

class GetPokemonByName @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Resource<PokemonDto> {
        return pokemonRepository.getPokemon(name)
    }
}