package com.sillazy.pokemon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Hilt로 앱 전체 의존성 주입

@HiltAndroidApp
class PokemonApplication : Application()