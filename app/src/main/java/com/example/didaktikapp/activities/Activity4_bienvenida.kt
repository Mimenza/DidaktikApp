package com.example.didaktikapp.activities

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.media.MediaPlayer
import android.os.Looper
import android.view.animation.TranslateAnimation
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity4BienvenidaBinding
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.coroutines.*
import java.util.*

class Activity4_bienvenida : AppCompatActivity() {

    private lateinit var binding: Activity4BienvenidaBinding
    private lateinit var audio: MediaPlayer
    private lateinit var vistaanimada: TranslateAnimation
    private var autoOpenMap: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding = Activity4BienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        writeText()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnv4Saltar.visibility = View.VISIBLE
        }, 5000)

        binding.btnv4Saltar.setOnClickListener {
            autoOpenMap = false
            comprobarPermisosMapa()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (autoOpenMap) {
                comprobarPermisosMapa()
            }
        }, 10000)

        audioSound()
    }

    private fun writeText() {
        //funcion que escribe el texto
        val typeWriterView = findViewById<View>(R.id.txtv4_bienvenida) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.text_bienvenida))
        typeWriterView.setDelay(70)
        binding.btnv4Saltar.visibility = View.GONE
    }

    private fun audioSound() {
        //funcion para la reproduccion del sonido
        runBlocking {
            launch {
                audio = MediaPlayer.create(this@Activity4_bienvenida, R.raw.sarrera)
                audio.start()
                audio.duration
                audio.setOnCompletionListener {
                    exitAnimationfun()
                }
            }
        }
        //animacion para la descripcion
        starAnimationfun()
    }

    override fun onStop() {
        audio.stop()
        super.onStop()
    }

    override fun onDestroy() {
        audio.stop()
        super.onDestroy()
    }

    private fun abrirMapa() {
        audio.stop()
        val intent = Intent(this, Activity5_Mapa::class.java)
        startActivity(intent)
        this.overridePendingTransition(0, 0)
        finish()
    }

    private fun comprobarPermisosMapa() {
        if (!Utils.comprobarPermisosMap(this)) {
            askForMapPermission()
        } else {
            abrirMapa()
        }
    }

    private fun starAnimationfun() {
        //animacion entrada upelio
        vistaanimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaanimada.duration = 2000
        imgv4_upelio.startAnimation(vistaanimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            imgv4_upelio.isVisible = false
            talkAnimationfun()
        }, 2000)
    }

    private fun talkAnimationfun() {
        imgv4_manzanatutorial.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = imgv4_manzanatutorial.background as AnimationDrawable
        ani.start()
    }

    private fun exitAnimationfun() {
        //escondemos la manzanda de la animacion
        imgv4_manzanatutorial.isVisible = false

        //animacion salido upelio
        vistaanimada = TranslateAnimation(0f, -1000f, 0f, 0f)
        vistaanimada.duration = 2000

        //vistaanimada.fillAfter = true
        imgv4_upelio.startAnimation(vistaanimada)

        Handler(Looper.getMainLooper()).postDelayed({
            comprobarPermisosMapa()
        }, 2000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] != -1) {
            abrirMapa()
        } else {
            finish()
        }
    }

    private fun askForMapPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
    }

}