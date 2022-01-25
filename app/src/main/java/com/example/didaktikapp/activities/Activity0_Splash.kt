package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.didaktikapp.databinding.Activity0SplashBinding

class Activity0_Splash : AppCompatActivity() {

    private lateinit var binding: Activity0SplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding = Activity0SplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recoge informacion de Shared preferences para el tema oscuro
        val sharedPreferences = getSharedPreferences("com.example.didaktikapp_preferences", 0)
        val numero: Int = sharedPreferences.getInt("io.github.manuelernesto.DARK_STATUS", 0)

        //Cambia entre modo oscuro y modo claro
        if (numero == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        }

        //Lanza la activity principal despues de 2,2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Activity1_Principal::class.java)
            startActivity(intent)
            this.overridePendingTransition(0, 0)
            finish()
        }, 2200)
    }
}
