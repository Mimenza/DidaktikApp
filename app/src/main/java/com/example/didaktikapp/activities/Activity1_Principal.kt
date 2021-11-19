package com.example.didaktikapp.activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.didaktikapp.databinding.Activity1PrincipalBinding
import kotlinx.android.synthetic.main.activity1_principal.*
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment5_ajustes

class Activity1_Principal : AppCompatActivity() {

    private lateinit var binding: Activity1PrincipalBinding
    private lateinit var audio: MediaPlayer
    var show: Int = 0
    var firstTime :Boolean= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity1_principal)

        binding = Activity1PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Boton para ver los ajustes
        btn1_ajustes.setOnClickListener() {
            showAjustes()
        }
        //Animacion manzana

        img1_upelio.setBackgroundResource(com.example.didaktikapp.R.drawable.animacion_manzana)
        val ani = img1_upelio.getBackground() as AnimationDrawable
        ani.start()
        //Animacion manzana fin


        //Audio principal
        runBlocking() {
            launch {
                audio = MediaPlayer.create(
                    this@Activity1_Principal,
                    com.example.didaktikapp.R.raw.abestia
                )
                audio.setVolume(0.15F, 0.15F)
                audio.start()
            }
        }

        //Audio principal fin

        menu()
        ocultarbtn()

        binding.btn1Menu.setOnClickListener() {
            //Boton para sacar el menu temporal
            ocultartodo()
            showbtn()
        }


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
            /*
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
             */

    }

    override fun onPause() {
        super.onPause()
        audio.pause()
    }

    override fun onResume() {
        super.onResume()
        audio.start()
    }

    override fun onRestart() {
        super.onRestart()
        runBlocking() {
            launch {
                audio = MediaPlayer.create(
                    this@Activity1_Principal,
                    com.example.didaktikapp.R.raw.abestia
                )
                audio.setVolume(0.15F, 0.15F)
                audio.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        audio.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        audio.stop()
    }

    private fun showAjustes() {
    val fragment :Fragment = Fragment5_ajustes()

        if (show == 0) {
            //el fragment esta oculto
            if(firstTime == true){
                //es la primera vez
                supportFragmentManager.beginTransaction().add(R.id.framelayout1, fragment).commit()
                firstTime = false
             }
             else {
                 //el fragment ya ha sido añadido
                framelayout1.isVisible= true
            }

            show = 1
            ocultartodo()
        } else {
            //el fragment es visible
            framelayout1.isVisible= false
            show = 0
            vertodo()

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

        binding.img1Upelio.isVisible = false
        binding.txtv1Gruponombre.isVisible = false
        binding.txtv1Titulo.isVisible = false
    }

    fun vertodo() {
        //ocultamos lo que no nos interesa
        binding.btn1Nuevo.isVisible = true
        binding.btn1Cargar.isVisible = true
        binding.img1Upelio.isVisible = true
        binding.txtv1Gruponombre.isVisible = true
        binding.txtv1Titulo.isVisible = true
    }

    fun showbtn(){
        //sacamos lo que nos interesa

        binding.btnMapa.isVisible = true
        binding.btnLogin.isVisible = true
        binding.btnLoad.isVisible = true
        binding.btnBienvenida.isVisible = true
    }
}


