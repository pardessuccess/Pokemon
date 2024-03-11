package com.sillazy.pokemon.domain.repository

import androidx.paging.PagingData
import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.model.PokemonListDto
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.data.model.Result
import com.sillazy.pokemon.domain.model.Pocket
import kotlinx.coroutines.flow.Flow

//포켓몬 구현체 상속 인터페이스

interface PokemonRepository {

    fun getPokemonList(): Flow<PagingData<Result>>

    suspend fun getPokemonListAtOnce(): Resource<PokemonListDto>

    suspend fun getPokemon(name: String): Resource<PokemonDto>

    suspend fun insertPocket(pocket: Pocket)

    suspend fun deletePocket(pocket: Pocket)

    fun getPockets() : Flow<List<Pocket>>

}