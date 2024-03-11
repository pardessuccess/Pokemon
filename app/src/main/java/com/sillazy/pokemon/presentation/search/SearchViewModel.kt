package com.sillazy.pokemon.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.model.PokemonListDto
import com.sillazy.pokemon.data.model.Result
import com.sillazy.pokemon.domain.usecases.pokemon.GetPokemonListAtOnce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getPokemonListAtOnce: GetPokemonListAtOnce
): ViewModel() {

    private val _pokemonAll = MutableStateFlow<Resource<PokemonListDto>>(Resource.Loading())
    val pokemonAll: StateFlow<Resource<PokemonListDto>> = _pokemonAll

    suspend fun getAllPokemon(){
         withContext(Dispatchers.IO) {
             _pokemonAll.emit(getPokemonListAtOnce())
         }
    }

    private var _pokemonList = MutableLiveData<List<Result>>(listOf())
    val pokemonList : LiveData<List<Result>> = _pokemonList

    fun setPokemonListShuffled(pokemonList: List<Result>){
        _pokemonList.value = pokemonList
    }

    init {
        viewModelScope.launch {
            getAllPokemon()
            _pokemonList.value = pokemonAll.value.data?.results?.shuffled()
        }
    }
}