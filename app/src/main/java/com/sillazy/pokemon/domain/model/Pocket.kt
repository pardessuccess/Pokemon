package com.sillazy.pokemon.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sillazy.pokemon.data.model.poke.Type
import kotlinx.parcelize.Parcelize

// 로컬 DB에 저장하는 포켓몬 데이터 모델

@Parcelize
@Entity
data class Pocket(
    @PrimaryKey val id: Int,
    val name: String,
    val types: String,
    val color: Int
) : Parcelable
