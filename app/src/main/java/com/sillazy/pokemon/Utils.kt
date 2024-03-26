package com.sillazy.pokemon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.sillazy.pokemon.data.model.poke.Stat
import com.sillazy.pokemon.data.model.poke.Type
import com.sillazy.pokemon.ui.theme.*
import java.util.Locale
import kotlin.text.StringBuilder


fun getImageUrl(pokemon: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon}.png"
fun getGifUrl(pokemon: String) =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/${pokemon}.gif"
// 그림에서 대표 색 추출

fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
    val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
    Palette.from(bmp).generate { palette ->
        palette?.dominantSwatch?.rgb?.let { colorValue ->
            onFinish(Color(colorValue))
        }
    }
}

// 포켓몬 이름 대문자 설정, - 기호 제거
fun setCapitalize(text: String): String {
    val new = StringBuilder(text.capitalize(Locale.ROOT))
    if (new.contains("-")) {
        val idxList = new.indexOfAll("-")
        idxList.forEach {
            new[it + 1] = new[it + 1].uppercaseChar()
            new.replace(it, it + 1, " ")
        }
    }
    return new.toString()
}

fun StringBuilder.indexOfAll(str: String): MutableList<Int> {
    var index = this.indexOf(str)
    val returnIndex = mutableListOf<Int>()

    while (index != -1) {
        returnIndex.add(index)
        index = this.indexOf(str, index + 1)
    }
    return returnIndex
}


//포켓몬 타입에 따른 이미지 설정
fun pokemonType(type: Type): Int {
    return when (type.type.name) {
        "bug" -> R.drawable.type_bug
        "dark" -> R.drawable.type_dark
        "dragon" -> R.drawable.type_dragon
        "electric" -> R.drawable.type_electric
        "fighting" -> R.drawable.type_fighting
        "fire" -> R.drawable.type_fire
        "flying" -> R.drawable.type_flying
        "ghost" -> R.drawable.type_ghost
        "grass" -> R.drawable.type_grass
        "ground" -> R.drawable.type_ground
        "ice" -> R.drawable.type_ice
        "normal" -> R.drawable.type_normal
        "poison" -> R.drawable.type_poison
        "psychic" -> R.drawable.type_psychic
        "rock" -> R.drawable.type_rock
        "steel" -> R.drawable.type_steel
        "water" -> R.drawable.type_water
        else -> R.drawable.pokeball
    }
}

//포켓몬 타입에 따른 색 설정
fun parseTypeToColor(type: String): Color {
    return when (type.toLowerCase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

//포켓몬 능력치에 따른 색 설정
fun parseStatToColor(stat: Stat): Color {
    return when (stat.stat.name.toLowerCase(Locale.ROOT)) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun toastText(context: Context, text: String) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}