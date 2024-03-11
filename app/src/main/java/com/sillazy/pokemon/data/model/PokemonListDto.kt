package com.sillazy.pokemon.data.model


//Retrofit을 이용하여 HTTP 통신으로 받아오는 포켓몬 리스트들 객체입니다.

data class PokemonListDto(
    val count: Int,
    val next: String,
    val previous: Any?,
    val results: List<Result>
)