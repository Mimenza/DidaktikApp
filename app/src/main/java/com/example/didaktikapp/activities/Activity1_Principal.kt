package com.example.didaktikapp.activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity1PrincipalBinding
import kotlinx.android.synthetic.main.activity1_principal.*
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import kotlinx.android.synthetic.main.activity4_bienvenida.imgv4_manzanatutorial
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class Activity1_Principal : AppCompatActivity() {

    private lateinit var binding: Activity1PrincipalBinding
    private lateinit var audio: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity1_principal)

        binding = Activity1PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Animacion manzana

        img1_upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = img1_upelio.getBackground() as AnimationDrawable
        ani.start()
        //Animacion manzana fin


        //Audio principal
        runBlocking() {
            launch {
                audio = MediaPlayer.create(this@Activity1_Principal, R.raw.abestia)
                audio.start()
            }
        }

        //Audio principal fin


        menu()
        ocultarbtn()

        binding.btn1Menu.setOnClickListener() {
            //Boton para sacar el menu temporal
            ocultartodo()
        }

        binding.btn1Hasi.setOnClickListener() {
            binding.btn1Hasi.isVisible = false
            binding.btn1Cargar.isVisible = true
            binding.btn1Nuevo.isVisible = true

            binding.btn1Nuevo.setOnClickListener() {
                //Nos lleva a la activity para hacer el login
                var i = Intent(this, Activity2_Login::class.java)
                startActivity(i)
            }

            binding.btn1Cargar.setOnClickListener() {
                //Nos lleva a la activity para ver todas las partidas creadas
                var i = Intent(this, Activity3_Load::class.java)
                startActivity(i)
            }

            runBlocking {
                launch {
                    try {
                        if (audio.isPlaying())
                            audio.stop()
                            audio.release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun ocultarbtn() {
        binding.btn1Cargar.isVisible = false
        binding.btn1Nuevo.isVisible = false
        binding.btnMapa.isVisible = false
        binding.btnLogin.isVisible = false
        binding.btnLoad.isVisible = false
        binding.btnBienvenida.isVisible = false
    }

    fun menu() {
        binding.btnBienvenida.setOnClickListener() {
            var i = Intent(this, Activity4_bienvenida::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
        binding.btnLoad.setOnClickListener() {
            var i = Intent(this, Activity3_Load::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
        binding.btnLogin.setOnClickListener() {
            var i = Intent(this, Activity2_Login::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
        binding.btnMapa.setOnClickListener() {
            var i = Intent(this, Activity5_Mapa::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
    }

    fun ocultartodo() {
        //ocultamos lo que no nos interesa
        binding.btn1Nuevo.isVisible = false
        binding.btn1Cargar.isVisible = false
        binding.btn1Hasi.isVisible = false
        binding.img1Upelio.isVisible = false
        binding.txtv1Gruponombre.isVisible = false
        binding.txtv1Titulo.isVisible = false

        //sacamos lo que nos interesa
        binding.btnMapa.isVisible = true
        binding.btnLogin.isVisible = true
        binding.btnLoad.isVisible = true
        binding.btnBienvenida.isVisible = true
    }
}


