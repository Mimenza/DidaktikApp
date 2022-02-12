package com.example.didaktikapp.Model


import android.content.Context

import android.preference.PreferenceManager

/**
 * Clase que guarda en sharedPreferences el idioma y el modo oscuro
 *
 * @constructor
 *
 *
 * @param context Fragment o activity en el que se recojen los datos
 */
class MyPreferences(context:Context) {
    //Variables del idioma y modo oscuro en shared preferences
    companion object {
        private const val DARK_STATUS = "io.github.manuelernesto.DARK_STATUS"
        private const val LANGUAGE ="language"
    }


    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var darkMode = preferences.getInt(DARK_STATUS, 0)
        //Aplica el modo oscuro o claro segun el numero metido
        set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()
    var lang = preferences.getInt(LANGUAGE, 0)
        //Aplica el idioma segun el numero metido
        set(value) = preferences.edit().putInt(LANGUAGE, value).apply()
}