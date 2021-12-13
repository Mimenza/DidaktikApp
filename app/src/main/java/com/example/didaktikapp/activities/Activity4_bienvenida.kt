package com.example.didaktikapp.activities

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.media.MediaPlayer
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity4BienvenidaBinding
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.coroutines.*
import java.util.*

data class ritmo(var tiempo: Int, var velocidad: Int)

class Activity4_bienvenida : AppCompatActivity() {

    private lateinit var binding: Activity4BienvenidaBinding
    private lateinit var audio: MediaPlayer
    private lateinit var vistaanimada: TranslateAnimation
    private lateinit var skipButtonHandler: Handler
    private lateinit var autoFinalizarHandler: Handler

    val ritmoList = listOf(
        ritmo(5000,10),
        ritmo(9500,70),
        ritmo(11000,10),
        ritmo(18000,70),
    )

    private var autoOpenMap: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity4_bienvenida)

        binding= Activity4BienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        writeText()

        Handler().postDelayed({
            binding.btnv4Saltar.visibility = View.VISIBLE
        }, 5000)

        binding.btnv4Saltar.setOnClickListener() {
            autoOpenMap = false
            comprobarPermisosMapa()
        }

        Handler().postDelayed({
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

        /*for (item in ritmoList) {
            var parado: Boolean = false
            Handler().postDelayed({
                parado = !parado
                if (parado) {
                    typeWriterView.removeAnimation()
                } else {
                    typeWriterView.animate()
                }
                println("****** VELOCIDAD CAMBIADA A: " + item.velocidad)
                //typeWriterView.setDelay(item.velocidad)
            }, item.tiempo.toLong())
        }*/
    }

    private fun audioSound() {
        //funcion para la reproduccion del sonido
        runBlocking() {
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
        if (!isMapPermissionGranted()) {
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
        Handler().postDelayed({
            imgv4_upelio.isVisible = false
            talkAnimationfun()
        }, 2000)
    }

    private fun talkAnimationfun() {
        imgv4_manzanatutorial.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = imgv4_manzanatutorial.getBackground() as AnimationDrawable
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

        Handler().postDelayed({
            comprobarPermisosMapa()
        }, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] != -1 ) {
            abrirMapa()
        } else {
            finish()
        }
    }

    fun isMapPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    fun askForMapPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

}