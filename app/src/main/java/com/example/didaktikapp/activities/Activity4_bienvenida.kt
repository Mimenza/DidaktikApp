package com.example.didaktikapp.activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.media.MediaPlayer
import android.widget.ImageView
import com.example.didaktikapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Activity4_bienvenida : AppCompatActivity() {
    private lateinit var appleAnimation:AnimationDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity4_bienvenida)
        getSupportActionBar()?.hide()

        //Animacion manzana
        val appleImage = findViewById<ImageView>(R.id.imgv4_manzanatutorial).apply {
            setBackgroundResource(R.drawable.animacion_manzana)
            appleAnimation = background as AnimationDrawable
        }

        appleImage.setOnClickListener({ appleAnimation.start() })
        //Animacion manzana fin


        Handler().postDelayed({
            val intent = Intent(this, Activity5_Mapa::class.java)
            startActivity(intent)
            this.overridePendingTransition(0, 0)
            finish()
        }, 43000)

        runBlocking() {
            launch {
                var ring: MediaPlayer = MediaPlayer.create(this@Activity4_bienvenida, R.raw.audio)
                ring.start()
            }
        }
    }
}