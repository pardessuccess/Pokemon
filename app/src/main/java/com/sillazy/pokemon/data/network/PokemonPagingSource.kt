package com.sillazy.pokemon.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.data.model.Result

//메인 화면에서 페이징을 통해 포켓몬 객체들을 받아오고, 스크롤에 맞게 적절히 전시합니다.

class PokemonPagingSource(
    private val pokemonApi: PokemonApi,
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalPokemonCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key ?: 1
        return try {
            val pokemonResponse = pokemonApi.getPokemonList()
            totalPokemonCount += pokemonResponse.results.size
            val pokemonList = pokemonResponse.results.distinctBy { it.name }
            LoadResult.Page(
                data = pokemonList,
                nextKey = if (totalPokemonCount == pokemonResponse.count) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }

}