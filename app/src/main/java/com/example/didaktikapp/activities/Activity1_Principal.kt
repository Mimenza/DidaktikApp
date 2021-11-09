package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.didaktikapp.R

import com.example.didaktikapp.databinding.Activity1PrincipalBinding

class Activity1_Principal : AppCompatActivity() {

    private lateinit var binding: Activity1PrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity1_principal)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding = Activity1PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menu()
        ocultarbtn()
        binding.btn1Hasi.setOnClickListener(){
            binding.btn1Hasi.isVisible=false
            binding.btn1Cargar.isVisible=true
            binding.btn1Nuevo.isVisible=true
            
            binding.btn1Cargar.setOnClickListener(){
                ocultartodo()
            }

        }

    }
    fun ocultarbtn(){
        binding.btn1Cargar.isVisible=false
        binding.btn1Nuevo.isVisible=false
        binding.btnMapa.isVisible=false
        binding.btnLogin.isVisible=false
        binding.btnLoad.isVisible=false
        binding.btnBienvenida.isVisible=false
    }

    fun menu(){
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

    fun ocultartodo(){
        //ocultamos lo que no nos interesa
        binding.btn1Nuevo.isVisible=false
        binding.btn1Cargar.isVisible=false
        binding.btn1Hasi.isVisible=false
        binding.img1Upelio.isVisible=false
        binding.txtv1Gruponombre.isVisible=false
        binding.txtv1Titulo.isVisible=false

        //sacamos lo que nos interesa
        binding.btnMapa.isVisible=true
        binding.btnLogin.isVisible=true
        binding.btnLoad.isVisible=true
        binding.btnBienvenida.isVisible=true
    }
}