package com.example.didaktikapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.activity7_juego2_results.*

class Activity7_Juego2_Results : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity7_juego2_results)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)


        //Recojemos los datos del intent en el fragment del juego 2
       val totalQuestions= intent.getIntExtra(Constantsjuego2.TOTAL_QUESTIONS, 0)
        val correctAnswers= intent.getIntExtra(Constantsjuego2.CORRECT_ANSWERS, 0)


        txtv7_scoreuser.text="Zure emaitza: $correctAnswers/$totalQuestions"


        //Boton finish que nos redirecciona al mapa

        btn7_terminar.setOnClickListener {
            val intent = Intent(this,Activity5_Mapa::class.java)
            startActivity(intent)
        }

    }
}