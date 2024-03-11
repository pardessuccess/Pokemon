package com.sillazy.pokemon.di

import com.sillazy.pokemon.data.repository.PokemonRepositoryImpl
import com.sillazy.pokemon.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//포켓몬 구현체를 모듈에 의존성 주입

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule{
    @Binds
    @Singleton
    abstract fun providePokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepository
}