package com.sillazy.pokemon.di

import android.app.Application
import androidx.room.Room
import com.sillazy.pokemon.Constants.BASE_URL
import com.sillazy.pokemon.data.local.PocketDao
import com.sillazy.pokemon.data.local.PocketDatabase
import com.sillazy.pokemon.data.network.PokemonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//앱의 모듈에 네트워크, 룸 데이터베이스, Dao를 싱글톤으로 의존성 주입

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiInstance(): PokemonApi {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePocketDatabase(
        application: Application
    ): PocketDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = PocketDatabase::class.java,
            name = "pocket_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePocketDao(
        pocketDatabase: PocketDatabase
    ): PocketDao = pocketDatabase.pocketDao
}