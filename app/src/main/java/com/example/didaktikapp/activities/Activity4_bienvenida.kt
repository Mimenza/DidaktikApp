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








class Activity4_bienvenida : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity4_bienvenida)
        getSupportActionBar()?.hide()

        //Animacion manzana

        imgv4_manzanatutorial.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = imgv4_manzanatutorial.getBackground() as AnimationDrawable
        ani.start()
        //Animacion manzana fin


        val typeWriterView = findViewById<View>(R.id.txtv4_bienvenida) as TypeWriterView
        typeWriterView.animateText(resources.getString(R.string.text_bienvenida))
        typeWriterView.setDelay(70)






        Handler().postDelayed({
            val intent = Intent(this, Activity5_Mapa::class.java)
            startActivity(intent)
            this.overridePendingTransition(0, 0)
            finish()
        }, 43000)

        runBlocking() {
            launch {
                var ring: MediaPlayer = MediaPlayer.create(this@Activity4_bienvenida, R.raw.sarrera)
                ring.start()
            }
        }
    }
}