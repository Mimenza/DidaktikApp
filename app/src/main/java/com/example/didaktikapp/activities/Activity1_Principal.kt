package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity0SplashBinding
import com.example.didaktikapp.databinding.Activity1PrincipalBinding

class Activity1_Principal : AppCompatActivity() {
    private lateinit var binding: Activity1PrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_principal)

        binding = Activity1PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBienvenida.setOnClickListener(){
            var i = Intent(this, Activity4_bienvenida::class.java)
            startActivity(i)
        }
        binding.btnLoad.setOnClickListener(){
            var i = Intent(this, Activity3_Load::class.java)
            startActivity(i)
        }
        binding.btnLogin.setOnClickListener(){
            var i = Intent(this, Activity2_Login::class.java)
            startActivity(i)
        }
        binding.btnMapa.setOnClickListener(){
            var i = Intent(this, Activity5_Mapa::class.java)
            startActivity(i)
        }
    }
}