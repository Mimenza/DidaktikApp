package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.media.MediaPlayer
import com.example.didaktikapp.R






class Activity4_bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity4_bienvenida)
        getSupportActionBar()?.hide()



        Handler().postDelayed({
            val intent = Intent(this, Activity5_Mapa::class.java)
            startActivity(intent)
            this.overridePendingTransition(0, 0)
            finish()
        }, 10000)

        var ring:MediaPlayer = MediaPlayer.create(this, R.raw.audio)
        ring.start()

    }

}