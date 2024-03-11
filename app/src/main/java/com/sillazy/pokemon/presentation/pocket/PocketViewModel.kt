package com.sillazy.pokemon.presentation.pocket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillazy.pokemon.domain.model.Pocket
import com.sillazy.pokemon.domain.usecases.pocket.DeletePocket
import com.sillazy.pokemon.domain.usecases.pocket.GetPockets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PocketViewModel @Inject constructor(
    private val getPockets: GetPockets,
    private val deletePocket: DeletePocket
) : ViewModel() {

    private val _pockets = MutableStateFlow<List<Pocket>>(emptyList())
    val pockets: StateFlow<List<Pocket>> = _pockets.asStateFlow()

    fun getAllPockets() {
        viewModelScope.launch {
            getPockets().collectLatest {
                _pockets.emit(it)
            }
        }
    }

    fun releasePocket(pocket: Pocket){
        viewModelScope.launch {
            deletePocket(pocket)
            getAllPockets()
        }
    }

}