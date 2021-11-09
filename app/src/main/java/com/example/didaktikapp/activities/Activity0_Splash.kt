package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity0SplashBinding

class Activity0_Splash : AppCompatActivity() {
    private lateinit var binding:Activity0SplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity0_splash)

        binding = Activity0SplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnMapa.setOnClickListener(){
            var mapa = Intent(this, Activity5_Mapa::class.java)
            startActivity(mapa)
        }
    }
}