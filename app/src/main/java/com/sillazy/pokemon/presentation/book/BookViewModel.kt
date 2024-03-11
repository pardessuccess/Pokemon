package com.sillazy.pokemon.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sillazy.pokemon.domain.usecases.pokemon.GetPokemonList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getPokemonList: GetPokemonList
) : ViewModel() {

    val pokemonList = getPokemonList().cachedIn(viewModelScope)



}