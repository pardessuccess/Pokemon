package com.sillazy.pokemon.data.model

import com.sillazy.pokemon.data.model.poke.Ability
import com.sillazy.pokemon.data.model.poke.Form
import com.sillazy.pokemon.data.model.poke.GameIndice
import com.sillazy.pokemon.data.model.poke.Move
import com.sillazy.pokemon.data.model.poke.Species
import com.sillazy.pokemon.data.model.poke.Sprites
import com.sillazy.pokemon.data.model.poke.Stat
import com.sillazy.pokemon.data.model.poke.Type

//Retrofit을 이용하여 HTTP 통신으로 받아오는 객체입니다.

data class PokemonDto(
    val abilities: List<Ability> = listOf(),
    val base_experience: Int = 0,
//    val forms: List<Form> = listOf(),
//    val game_indices: List<GameIndice> = listOf(),
    val height: Int = 0,
//    val held_items: List<Any> = listOf(),
    val id: Int = 0,
    val is_default: Boolean = false,
//    val location_area_encounters: String = "",
    val moves: List<Move> = listOf(),
    val name: String = "",
    val order: Int = 0,
//    val past_abilities: List<Any> = listOf(),
//    val past_types: List<Any> = listOf(),
    val species: Species = Species("",""),
//    val sprites: Sprites,
    val stats: List<Stat> = listOf(),
    val types: List<Type> = listOf(),
    val weight: Int = 0
)