package com.sillazy.pokemon.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.local.PocketDao
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.data.model.PokemonListDto
import com.sillazy.pokemon.data.model.Result
import com.sillazy.pokemon.data.network.PokemonApi
import com.sillazy.pokemon.data.network.PokemonPagingSource
import com.sillazy.pokemon.domain.model.Pocket
import com.sillazy.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//포켓몬 데이터를 네트워크에서 불러오는 것과 로컬 데이터베이스에서 포켓몬 데이터를 CRUD하는 것을 구현하였습니다.

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pocketDao: PocketDao
) : PokemonRepository {
    override fun getPokemonList(): Flow<PagingData<Result>> {
        return Pager(
            config = PagingConfig(pageSize = 16),
            pagingSourceFactory = {
                PokemonPagingSource(pokemonApi = pokemonApi)
            }
        ).flow
    }

    override suspend fun getPokemonListAtOnce(): Resource<PokemonListDto> {
        val response = try {
            pokemonApi.getPokemonList()
        } catch (e: Exception) {
            return Resource.Error("Get Pokemon List At Once error occurred.")
        }
        return Resource.Success(response)
    }


    override suspend fun getPokemon(name: String): Resource<PokemonDto> {
        val response = try {
            pokemonApi.getPokemon(name)
        } catch (e: Exception) {
            return Resource.Error("Get Pokemon error occurred.")
        }
        return Resource.Success(response)
    }

    override suspend fun insertPocket(pocket: Pocket) {
        pocketDao.insert(pocket)
    }

    override suspend fun deletePocket(pocket: Pocket) {
        pocketDao.delete(pocket)
    }

    override fun getPockets(): Flow<List<Pocket>> {
        return pocketDao.getPockets()
    }
}