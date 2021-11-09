package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity0SplashBinding

class Activity0_Splash : AppCompatActivity() {
    private lateinit var binding:Activity0SplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity0_splash)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding = Activity0SplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

                Handler().postDelayed({
                    val intent = Intent(this, Activity1_Principal::class.java)
                    startActivity(intent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }, 2000) // 2000 is the delayed time in milliseconds.

    }
}