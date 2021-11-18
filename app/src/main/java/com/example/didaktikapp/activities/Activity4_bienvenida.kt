package com.example.didaktikapp.activities

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.media.MediaPlayer
import android.text.TextUtils


import kotlinx.android.synthetic.main.activity4_bienvenida.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.example.didaktikapp.R
import android.text.method.ScrollingMovementMethod
import com.example.didaktikapp.databinding.Activity2LoginBinding
import com.example.didaktikapp.databinding.Activity4BienvenidaBinding
import java.lang.System.`in`


class Activity4_bienvenida : AppCompatActivity() {

    private lateinit var binding: Activity4BienvenidaBinding

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
        //binding.btnv4Saltar.isEnabled = false

        Handler().postDelayed({
            binding.btnv4Saltar.visibility = View.VISIBLE
        }, 5000)

        binding.btnv4Saltar.setOnClickListener() {
            abrirMapa()
        }

        Handler().postDelayed({
            abrirMapa()
        }, 43000)

        runBlocking() {
            launch {
                var ring: MediaPlayer = MediaPlayer.create(this@Activity4_bienvenida, R.raw.sarrera)
                ring.start()
            }
        }
    }

    private fun abrirMapa() {
        val intent = Intent(this, Activity5_Mapa::class.java)
        startActivity(intent)
        this.overridePendingTransition(0, 0)
        finish()
    }
}