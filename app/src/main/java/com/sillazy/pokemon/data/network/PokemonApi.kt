package com.sillazy.pokemon.data.network

import com.sillazy.pokemon.data.model.PokemonListDto
import com.sillazy.pokemon.data.model.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {

    @GET("pokemon/?offset=0&limit=1302")
    suspend fun getPokemonList(): PokemonListDto

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): PokemonDto

}