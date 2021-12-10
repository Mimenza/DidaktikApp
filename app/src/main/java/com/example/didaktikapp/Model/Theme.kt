package com.example.didaktikapp.Model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.example.didaktikapp.R

class Theme {

    fun checkTheme(context:Context, button:Button) {

        var sharedPreferences = context.getSharedPreferences("modoclaro", 0)
        var editor = sharedPreferences.edit()

        if (sharedPreferences.contains("tipo")) {
            var tipoTema = sharedPreferences.getString("tipo", "")
            if (tipoTema != "modoclaro") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                button.text = context.getResources().getString(R.string.botonclaro)
                editor.putString("tipo", "modoclaro").apply()

            }
         else if(tipoTema == "modoclaro"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            editor.putString("tipo", "modooscuro").apply()
        }
    }}
}