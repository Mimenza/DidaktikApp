package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.didaktikapp.Model.MyPreferences
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity0SplashBinding


class Activity0_Splash : AppCompatActivity() {
    private lateinit var binding: Activity0SplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity0_splash)

        binding = Activity0SplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Handler().postDelayed({
            val aniFade = AnimationUtils.loadAnimation(this, R.anim.zoom)
            binding.txtv0Upelio.startAnimation(aniFade)

        }, 2000)*/
        //Shared preferences tema oscuro
        val sharedPreferences = getSharedPreferences("com.example.didaktikapp_preferences", 0)
        val numero:Int = sharedPreferences.getInt("io.github.manuelernesto.DARK_STATUS", 0)
        if (numero==0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()

        }
        //Shared preferences tema oscuro fin
        Handler().postDelayed({

            val intent = Intent(this, Activity1_Principal::class.java)
            startActivity(intent)
            this.overridePendingTransition(0, 0)
            finish()
        }, 2200)
    }
}