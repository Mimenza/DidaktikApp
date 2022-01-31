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
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment4_ajustes

class Activity1_Principal : AppCompatActivity() {

    private lateinit var binding: Activity1PrincipalBinding
    private var fragment: Fragment? = null
    private var audio: MediaPlayer? = null
    private var ajustesShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val sharedPreferences = this.getSharedPreferences("com.example.didaktikapp_preferences", 0)
        val darkStatus = sharedPreferences?.getInt("io.github.manuelernesto.DARK_STATUS",0)

        if(darkStatus == 0){
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        binding = Activity1PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Boton para ver los ajustes
        btn1_ajustes.setOnClickListener {
            showAjustes()
        }

        //Animacion manzana
        img1_upelio.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = img1_upelio.getBackground() as AnimationDrawable
        ani.start()

        //Audio principal
        runBlocking {
            launch {
                audio = MediaPlayer.create(this@Activity1_Principal, R.raw.abestia)
                audio?.setVolume(0.2F, 0.2F)
                audio?.start()
            }
        }

        binding.btn1Cargar.isVisible = true
        binding.btn1Nuevo.isVisible = true

        binding.btn1Nuevo.setOnClickListener {
            //Nos lleva a la activity para hacer el login
            val i = Intent(this, Activity2_Login::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }

        binding.btn1Cargar.setOnClickListener {
            //Nos lleva a la activity para ver todas las partidas creadas
            val i = Intent(this, Activity3_Load::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        if(audio?.isPlaying == true){
            audio?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: preguntar si esta el audio empezado
        audio?.start()
    }

    override fun onRestart() {
        super.onRestart()
        runBlocking {
            launch {
                audio = MediaPlayer.create(this@Activity1_Principal, R.raw.abestia)
                audio?.setVolume(0.2F, 0.2F)
                audio?.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        audio?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        audio?.release()
    }

    override fun onBackPressed() {
        if (fragment != null) {
            cerrarAjustes()
        } else {
            super.onBackPressed()
        }
    }

    private fun cerrarAjustes() {
        supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        vertodo()
        fragment = null
        ajustesShowing = false
    }

    private fun showAjustes() {
        fragment = Fragment4_ajustes()
        supportFragmentManager.beginTransaction().add(R.id.cl1_principal, fragment!!).commit()
        ocultartodo()
    }


    private fun ocultartodo() {
        //ocultamos lo que no nos interesa
        binding.btn1Nuevo.isVisible = false
        binding.btn1Cargar.isVisible = false
        binding.img1Upelio.isVisible = false
        binding.txtv1Gruponombre.isVisible = false
        binding.txtv1Titulo.isVisible = false
        binding.btn1Ajustes.isVisible = false
    }

    /**
     * Muestra los botones
     */
    private fun vertodo() {
        //ocultamos lo que no nos interesa
        binding.btn1Nuevo.isVisible = true
        binding.btn1Cargar.isVisible = true
        binding.img1Upelio.isVisible = true
        binding.txtv1Gruponombre.isVisible = true
        binding.txtv1Titulo.isVisible = true
        binding.btn1Ajustes.isVisible = true
    }
}



