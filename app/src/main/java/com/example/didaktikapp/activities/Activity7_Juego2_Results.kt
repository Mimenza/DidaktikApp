package com.example.didaktikapp.activities


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.R


import com.example.didaktikapp.fragments.juegos.Fragment1_2_juego
import com.example.didaktikapp.fragments.minijuegos.Fragment2_2_minijuego

import kotlinx.android.synthetic.main.activity7_juego2_results.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Activity7_Juego2_Results : AppCompatActivity() {
    private var fragment :Fragment? = null

    var minijuegoShowing: Boolean = false
    var juegoShowing: Boolean = false
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



        var audio: MediaPlayer
     if (correctAnswers>=2) {
         //Audio acierto
         runBlocking() {
             launch {
                 audio = MediaPlayer.create(this@Activity7_Juego2_Results, R.raw.ongiaudioa)
                 audio.start()
             }
         }
        btn7_terminar.setOnClickListener {
             //Mostramos fragement minijuego
             showMinijuego2()
            //Ocultamos los botones
            ocultaracciones()
         }
         //Ocultamos boton intentar de nuevo

         btn7_saiatuberriro.isVisible=false
     }else{
         //Audio error
         runBlocking() {
             launch {
                 audio = MediaPlayer.create(this@Activity7_Juego2_Results, R.raw.gaizkiaudioa)
                 audio.start()
             }
         }
         btn7_terminar.isVisible=false
         btn7_saiatuberriro.setOnClickListener {

               //Mostramos fragment juego2
               showFragmentJuego2()
              //Ocultamos los botones
               ocultaracciones()

         }
     }
    }

    fun showFragmentJuego2(){

        if (juegoShowing) {
            return
        }
        juegoShowing = true
        fragment = Fragment1_2_juego()
        supportFragmentManager.beginTransaction().add(R.id.framelayoutjuego2results, fragment!!).commit()

    }
    fun showMinijuego2(){

        if (minijuegoShowing) {
            return
        }
        minijuegoShowing = true
        fragment = Fragment2_2_minijuego()
        supportFragmentManager.beginTransaction().add(R.id.framelayoutjuego2results, fragment!!).commit()

    }

    fun ocultaracciones(){
        btn7_terminar.isVisible=false
        btn7_saiatuberriro.isVisible=false
        txtv7_result.isVisible=false
        img1_2_trofeo.isVisible=false
        txtv7_scoreuser.isVisible=false
    }
}