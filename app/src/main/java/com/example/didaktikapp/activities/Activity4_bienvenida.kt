package com.example.didaktikapp.activities

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.media.MediaPlayer

import kotlinx.android.synthetic.main.activity4_bienvenida.*
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity4BienvenidaBinding
import kotlinx.coroutines.*
import java.util.*

data class ritmo(var tiempo: Int, var velocidad: Int)

class Activity4_bienvenida : AppCompatActivity() {

    private lateinit var binding: Activity4BienvenidaBinding
    private lateinit var ring: MediaPlayer

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
        getSupportActionBar()?.hide()

        binding= Activity4BienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Animacion manzana

        imgv4_manzanatutorial.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = imgv4_manzanatutorial.getBackground() as AnimationDrawable
        ani.start()
        //Animacion manzana fin

        val typeWriterView = findViewById<View>(R.id.txtv4_bienvenida) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.text_bienvenida))
        typeWriterView.setDelay(70)
        binding.btnv4Saltar.visibility = View.GONE

        for (item in ritmoList) {
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
        }


        Handler().postDelayed({
            binding.btnv4Saltar.visibility = View.VISIBLE
        }, 5000)

        binding.btnv4Saltar.setOnClickListener() {
            autoOpenMap = false
            abrirMapa()
        }

        Handler().postDelayed({
            if (autoOpenMap) {
                abrirMapa()
            }
        }, 10000)

        runBlocking() {
            launch {
                ring = MediaPlayer.create(this@Activity4_bienvenida, R.raw.sarrera)
                ring.start()
                ring.duration
            }
        }
    }

    private fun abrirMapa() {
        ring.stop()
        val intent = Intent(this, Activity5_Mapa::class.java)
        startActivity(intent)
        this.overridePendingTransition(0, 0)
        finish()
    }
}