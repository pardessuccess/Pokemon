package com.sillazy.pokemon.presentation.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.domain.model.Pocket
import com.sillazy.pokemon.domain.usecases.pocket.InsertPocket
import com.sillazy.pokemon.domain.usecases.pokemon.GetPokemonByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonByName: GetPokemonByName,
    private val insertPocket: InsertPocket
) : ViewModel() {

    var level = 1

    init {
        level = Random.nextInt(1, 50)
    }

    private var _pokemon = MutableLiveData(PokemonDto())
    val pokemon: LiveData<PokemonDto> = _pokemon

    suspend fun getPokemon(name: String): Resource<PokemonDto> {
        delay(500)
        return getPokemonByName(name)
    }

    private var _hp = MutableLiveData(100)
    val hp: LiveData<Int> = _hp

    fun setPokemon(pokemonDto: PokemonDto) {
        _pokemon.value = pokemonDto
        _hp.value = pokemonDto.stats[0].base_stat
        setNumber(pokemonDto.id)
    }

    private var _number = MutableLiveData(1)
    val number: LiveData<Int> = _number

    private fun setNumber(num: Int) {
        _number.value = num
    }

    fun catchPokemon() {
        viewModelScope.launch {
            pokemon.value?.let {
                val pocket = Pocket(
                    it.id,
                    it.name,
                    it.types.toString(),
                    backgroundColor.value ?: Color.White.toArgb()
                )
                insertPocket(pocket)
                println(pocket)
            }
        }
    }

    private var _backgroundColor = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int> = _backgroundColor

    fun setBackgroundColor(color: Color) {
        _backgroundColor.value = color.toArgb()
    }
}